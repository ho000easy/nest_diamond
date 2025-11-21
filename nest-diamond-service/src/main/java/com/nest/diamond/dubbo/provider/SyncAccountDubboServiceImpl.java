package com.nest.diamond.dubbo.provider;

import com.google.common.collect.Maps;
import com.nest.diamond.common.enums.AccountType;
import com.nest.diamond.common.enums.WalletVendor;
import com.nest.diamond.dubbo.api.SyncAccountDubboService;
import com.nest.diamond.dubbo.dto.RpcResult;
import com.nest.diamond.dubbo.dto.sync.AccountRef;
import com.nest.diamond.dubbo.dto.sync.SyncAccountRequest;
import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.domain.Seed;
import com.nest.diamond.service.AccountService;
import com.nest.diamond.service.SeedService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@DubboService
public class SyncAccountDubboServiceImpl implements SyncAccountDubboService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private SeedService seedService;

    @Transactional
    @Override
    public RpcResult<Void> syncAccount(SyncAccountRequest syncAccountRequest) {
        List<AccountRef> accountRefList = syncAccountRequest.getAccounts();
        List<String> toBeSyncedAccountAddress = accountRefList.stream().map(AccountRef::getAddress).toList();
        List<Account> accountList = accountService.findByAddresses(toBeSyncedAccountAddress);
        Map<String, Account> accountMap = Maps.uniqueIndex(accountList, Account::getAddress);
        List<AccountRef> filteredAccountRefList = accountRefList.stream().filter(accountRef -> {
            return !accountMap.containsKey(accountRef.getAddress());
        }).toList();

        List<String> seedPrefixList = filteredAccountRefList.stream().map(AccountRef::getSeedPrefix).distinct().toList();
        seedPrefixList.forEach(seedPrefix -> {
            Seed seed = seedService.findBySeedPrefix(seedPrefix);
            if(seed == null){
                seedService.createSeed(seedPrefix);
            }
        });
        List<Seed> allSeedList = seedService.allSeeds();
        Map<String, Seed> seedMap = Maps.uniqueIndex(allSeedList, Seed::getSeedPrefix);

        List<Account> toBeInsertedAccountList = filteredAccountRefList.stream().map(accountRef -> {
            Account account = new Account();
            BeanUtils.copyProperties(accountRef, account);
            account.setWalletVendor(WalletVendor.valueOf(accountRef.getWalletVendor()));
            account.setType(AccountType.valueOf(accountRef.getType()));

            Seed seed = seedMap.get(accountRef.getSeedPrefix());
            account.setSeedId(seed.getId());
            account.setSeedPrefix(seed.getSeedPrefix());
            return account;
        }).toList();
        accountService.batchInsert(toBeInsertedAccountList);
        return RpcResult.success();
    }
}
