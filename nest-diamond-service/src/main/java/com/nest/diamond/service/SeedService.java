package com.nest.diamond.service;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nest.diamond.common.enums.AccountType;
import com.nest.diamond.common.enums.WalletGenerateType;
import com.nest.diamond.common.enums.WalletVendor;
import com.nest.diamond.common.util.Bip44Util;
import com.nest.diamond.common.util.concurrent.ParallelUtil;
import com.nest.diamond.iservice.SeedIService;
import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.domain.Seed;
import com.nest.diamond.model.domain.query.SeedQuery;
import com.nest.diamond.model.vo.AddSeedReq;
import lombok.extern.slf4j.Slf4j;
import org.p2p.solanaj.core.SolanaAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class SeedService {

    @Autowired
    private SeedIService seedIService;

    @Autowired
    private AccountService accountService;

    private final ImmutableMap<WalletVendor, WalletEngine> walletVendorWalletEngineImmutableMap = ImmutableMap.of(
            WalletVendor.METAMASK, new MetaMaskWalletEngine(),
            WalletVendor.PHANTOM, new PhantomWalletEngine()
    );

    public List<Seed> allSeeds() {
        return seedIService.selectAll();
    }

    public String generateSeed(){
        return Bip44Util.generateSeed();
    }

    public Seed findById(Long id) {
        return seedIService.getById(id);
    }

    public Seed findBySeedPrefix(String seedPrefix){
        return seedIService.findBySeedPrefix(seedPrefix);
    }

    public List<Seed> search(SeedQuery seedQuery) {
        return seedIService.search(seedQuery);
    }


    public void createSeed(String seedPrefix, WalletVendor walletVendor, WalletGenerateType walletGenerateType) {
        Seed seed = new Seed();
        seed.setSeedPrefix(seedPrefix);
        seed.setWalletGenerateType(walletGenerateType);
        seed.setWalletVendor(walletVendor);
        seedIService.save(seed);
    }

    @Transactional
    public void createAccounts(AddSeedReq addSeedReq) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        String seedPrefix = null;
        if (addSeedReq.getWalletGenerateType() == WalletGenerateType.MULTI_PRIVATE_KEY
                || addSeedReq.getWalletGenerateType() == WalletGenerateType.MULTI_ADDRESS) {
            String seed = Bip44Util.generateSeed();
            String[] words = seed.split(" ");
            seedPrefix = words[0] + " " + words[1];
        } else {
            seedPrefix = seedPrefix(addSeedReq.getSeedWordsList().get(0));
        }

        createSeed(seedPrefix, addSeedReq.getWalletVendor(), addSeedReq.getWalletGenerateType());

        Seed _seed = seedIService.findBySeedPrefix(seedPrefix);
        log.info("插入seed耗时 {}", stopwatch.stop());

        stopwatch.reset().start();

        List<Account> accountList = buildAccounts(addSeedReq, _seed);
        log.info("构建账户耗时 {}", stopwatch.stop());
        accountList.sort(Comparator.comparing(Account::getHdIndex));

        stopwatch.reset().start();
        accountService.batchInsert(accountList);
        log.info("批量插入账户耗时 {}", stopwatch.stop());
    }

    public List<Account> buildAccounts(AddSeedReq addSeedReq, Seed seed) {
        WalletEngine walletEngine = walletVendorWalletEngineImmutableMap.get(addSeedReq.getWalletVendor());
        if(addSeedReq.getWalletGenerateType() == WalletGenerateType.SINGLE_SEED){
            String seedWords = addSeedReq.getSeedWordsList().get(0);
            Assert.isTrue(MnemonicUtils.validateMnemonic(seedWords), "seed 格式不正确");
            return walletEngine.createFromSeed(seed, seedWords, addSeedReq.getCount(), false);
        }
        if(addSeedReq.getWalletGenerateType() == WalletGenerateType.MULTI_ADDRESS) {
            return batchBuildCustodyAccount(addSeedReq.getAddressList(), seed);
        }
        if(addSeedReq.getWalletGenerateType() == WalletGenerateType.MULTI_SEED) {
            ConcurrentMap<Integer, Account> concurrentMap = Maps.newConcurrentMap();
            List<Callable<Object>> callableList = Lists.newArrayList();
            for (int i = 0; i < addSeedReq.getSeedWordsList().size(); i++) {
                final int j = i;
                String seedWords = addSeedReq.getSeedWordsList().get(j);
                Callable<Object> callable = () -> {
                    try{
                        log.info("开始创建账户HD index {}", j + 1);
                        List<Account> accountList = walletEngine.createFromSeed(seed, seedWords, 1, addSeedReq.getIsReserveSeed());
                        if(accountList.get(0) == null){
                            Assert.notNull(accountList.get(0), "创建账户失败");
                        }
                        accountList.get(0).setHdIndex(j + 1);
                        concurrentMap.put(accountList.get(0).getHdIndex(), accountList.get(0));
                        log.info("创建账户结束HD index {}", j + 1);

                    }catch (Throwable throwable){
                        log.error("创建账户失败 HD index {}", (j + 1), throwable);
                    }
                    return null;
                };
                callableList.add(callable);
            }
            ParallelUtil.parallelExecute(20, callableList);
            List<Account> allAccountList = Lists.newArrayList();
            allAccountList.addAll(concurrentMap.values());
            Set<Integer> hdIndexSet = allAccountList.stream().map(Account::getHdIndex).collect(Collectors.toSet());
            List<Integer> missHDIndexList = IntStream.rangeClosed(1, addSeedReq.getSeedWordsList().size()).boxed().filter(num -> {
                return !hdIndexSet.contains(num);
            }).collect(Collectors.toList());
            log.info("遗漏的账户 {}", missHDIndexList);
            return allAccountList;
        }
        if(addSeedReq.getWalletGenerateType() == WalletGenerateType.MULTI_PRIVATE_KEY) {
            return createFromPrivateKeys(addSeedReq.getWalletVendor(), seed, addSeedReq.getPrivateKeyList());
        }
        throw new IllegalArgumentException("未知的钱包生成类型");
    }

    public List<Account> batchBuildCustodyAccount(List<String> addressList, Seed seed) {
        List<Account> accountList = accountService.findByAddresses(addressList);
        Collections.sort(accountList, Comparator.comparing(Account::getHdIndex));
        int maxSeedIndex = accountList.isEmpty() ? 0 : accountList.get(accountList.size() - 1).getHdIndex();
        Map<String, Account> accountMap = Maps.uniqueIndex(accountList, Account::getAddress);
        List<String> filteredAddressList = addressList.stream().filter(address -> !accountMap.containsKey(address)).collect(Collectors.toList());
        List<Account> __accountList = Lists.newArrayList();
        for(int i = 0; i < filteredAddressList.size(); i++){
            Account account = new Account();
            account.setAddress(filteredAddressList.get(i));
            account.setWalletVendor(WalletVendor.CUSTODY);
            account.setSeedId(seed.getId());
            account.setSeedPrefix(seed.getSeedPrefix());
            account.setType(AccountType.CUSTODY);
            account.setHdIndex(i + maxSeedIndex + 1);
            __accountList.add(account);
        }

        return __accountList;
    }

    private List<Account> createFromPrivateKeys(WalletVendor walletVendor, Seed seed, List<String> privateKeyList){
        List<Callable<Account>> callableList = Lists.newArrayList();
        for(int i = 0; i < privateKeyList.size(); i++){
            int finalI = i;
            callableList.add(() -> {
                String privateKey = privateKeyList.get(finalI);
                int index = finalI + 1;
                Credentials credentials = Credentials.create(privateKey);
                Account account = new Account();
                account.setSeedId(seed.getId());
                account.setHdIndex(index);
                account.setPrivateKey(privateKey);
                account.setAddress(credentials.getAddress());
                account.setSeedPrefix(seed.getSeedPrefix());
                account.setType(AccountType.NON_CUSTODY);
                account.setWalletVendor(walletVendor);
                return account;
            });
        }
        List<Account> accountList = ParallelUtil.parallelExecute(20, callableList);

        List<String> addressList = accountList.stream().map(Account::getAddress).collect(Collectors.toList());
        List<Account> _accountList = accountService.findByAddresses(addressList);
        Map<String, Account> accountMap = Maps.uniqueIndex(_accountList, Account::getAddress);
        accountList = accountList.stream().filter(account -> {
            return !accountMap.containsKey(account.getAddress());
        }).collect(Collectors.toList());

        return Lists.newArrayList(accountList);
    }

    interface WalletEngine {
        List<Account> createFromSeed(Seed seed, String seedWords, Integer count, Boolean isReserveSeed);
    }

    public class MetaMaskWalletEngine implements WalletEngine {
        @Override
        public List<Account> createFromSeed(Seed seed, String seedWords, Integer count, Boolean isReserveSeed) {
            List<Callable<Account>> callableList = IntStream.range(0, count).mapToObj(i -> (Callable<Account>) () -> {
                try{
                    if (i % 1000 == 0) {
                        log.info("种子构建第{}个账户", i);
                    }
                    Credentials credentials = Bip44Util.getFixedHDCredentials(seedWords, i);
                    String privateKey = Bip44Util.getPrivateKey(credentials);
                    String address = credentials.getAddress();
                    Account account = new Account();
                    account.setSeedId(seed.getId());
                    account.setHdIndex(i + 1);
                    account.setPrivateKey(privateKey);
                    account.setAddress(address);
                    if(isReserveSeed){
                        account.setSeed(seedWords);
                    }
                    account.setSeedPrefix(seed.getSeedPrefix());
                    account.setType(AccountType.NON_CUSTODY);
                    account.setWalletVendor(WalletVendor.METAMASK);
                    return account;
                }catch (Throwable throwable){
                    log.error("构建种子失败", throwable);
                }
                return null;
            }).collect(Collectors.toList());
            List<Account> accountList = ParallelUtil.parallelExecute("seed-create-account-pool-%d", 20, callableList);
            return accountList.stream().sorted(Comparator.comparing(Account::getHdIndex)).collect(Collectors.toList());

        }
    }


    public class PhantomWalletEngine implements WalletEngine {
        @Override
        public List<Account> createFromSeed(Seed seed, String seedWords, Integer count, Boolean isReserveSeed) {
            List<Callable<Account>> callableList = IntStream.range(0, count).mapToObj(i -> (Callable<Account>) () -> {
                try{
                    if (i % 1000 == 0) {
                        log.info("种子构建第{}个账户", i);
                    }
                    List<String> wordList = Arrays.asList(seedWords.split(" "));
                    SolanaAccount solanaAccount = SolanaAccount.fromBip44MnemonicWithChange(wordList, "", i);

                    Account account = new Account();
                    account.setSeedId(seed.getId());
                    account.setHdIndex(i + 1);
                    account.setPrivateKey(solanaAccount.getPrivateKey());
                    account.setAddress(solanaAccount.getAddress());
                    account.setSeedPrefix(seed.getSeedPrefix());
                    account.setType(AccountType.NON_CUSTODY);
                    account.setWalletVendor(WalletVendor.PHANTOM);
                    return account;
                }catch (Throwable throwable){
                    log.error("构建种子失败", throwable);
                }
                return null;
            }).collect(Collectors.toList());
            List<Account> accountList = ParallelUtil.parallelExecute("seed-create-account-pool-%d", 20, callableList);
            return accountList.stream().sorted(Comparator.comparing(Account::getHdIndex)).collect(Collectors.toList());

        }
    }

    private String seedPrefix(String seedWords) {
        String[] words = seedWords.split(" ");
        return String.format("%s %s", words[0], words[1]);
    }


    @Transactional
    public void deleteById(Long id) {
        List<Long> accountIds = accountService.findBySeedId(id).stream().map(Account::getId).collect(Collectors.toList());
        accountService.deleteByIds(accountIds);
        seedIService.removeById(id);
        System.out.println();
    }

    @Transactional
    public void deleteByIds(List<Long> ids) {
        ids.forEach(id -> deleteById(id));
    }
}
