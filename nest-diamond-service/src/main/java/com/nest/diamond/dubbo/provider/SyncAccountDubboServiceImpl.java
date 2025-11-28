package com.nest.diamond.dubbo.provider;

import com.google.common.collect.Maps;
import com.nest.diamond.common.enums.AccountType;
import com.nest.diamond.common.enums.TicketTokenStatusEnum;
import com.nest.diamond.common.enums.WalletVendor;
import com.nest.diamond.dubbo.api.SyncAccountDubboService;
import com.nest.diamond.dubbo.dto.RpcResult;
import com.nest.diamond.dubbo.dto.sync.AccountRef;
import com.nest.diamond.dubbo.dto.sync.SyncDiamondAccountsToLocalRequest;
import com.nest.diamond.dubbo.dto.sync.SyncDiamondAccountsToLocalResponse;
import com.nest.diamond.dubbo.dto.sync.SyncLocalAccountsToDiamondRequest;
import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.domain.Seed;
import com.nest.diamond.model.domain.Ticket;
import com.nest.diamond.model.domain.TicketToken;
import com.nest.diamond.model.domain.query.AirdropItemQuery;
import com.nest.diamond.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@DubboService
public class SyncAccountDubboServiceImpl implements SyncAccountDubboService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private SeedService seedService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketTokenService ticketTokenService;
    @Autowired
    private AirdropItemService airdropItemService;

    @Transactional
    @Override
    public RpcResult<Void> syncLocalAccountsToDiamond(SyncLocalAccountsToDiamondRequest syncLocalAccountsToDiamondRequest) {
        log.info("同步账户数据开始");
        List<AccountRef> accountRefList = syncLocalAccountsToDiamondRequest.getAccounts();
        List<String> toBeSyncedAccountAddress = accountRefList.stream().map(AccountRef::getAddress).toList();
        List<Account> accountList = accountService.findByAddresses(toBeSyncedAccountAddress);
        Map<String, Account> accountMap = Maps.uniqueIndex(accountList, Account::getAddress);
        List<AccountRef> filteredAccountRefList = accountRefList.stream().filter(accountRef -> {
            return !accountMap.containsKey(accountRef.getAddress());
        }).toList();
        if(filteredAccountRefList.isEmpty()){
            return RpcResult.fail("已经同步过");
        }
        List<String> seedPrefixList = filteredAccountRefList.stream().map(AccountRef::getSeedPrefix).distinct().toList();
        seedPrefixList.forEach(seedPrefix -> {
            Assert.isTrue(StringUtils.isNotEmpty(seedPrefix), "seed prefix不能为空");
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
        log.info("同步账户数据结束");

        return RpcResult.success();
    }

    @Override
    public RpcResult<SyncDiamondAccountsToLocalResponse> syncDiamondAccountsToLocal(SyncDiamondAccountsToLocalRequest syncDiamondAccountsToLocalRequest) {
        Ticket ticket = ticketService.findByTicketNo(syncDiamondAccountsToLocalRequest.getTicketNo());
        ticketService.validateTicket(ticket);
        TicketToken ticketToken = ticketTokenService.findByTicketNoAndToken(ticket.getTicketNo(), syncDiamondAccountsToLocalRequest.getAuthToken());
        ticketTokenService.checkTicketToken(ticketToken);
        ticketTokenService.invalidate(ticketToken.getId());
        AirdropItemQuery airdropItemQuery = new AirdropItemQuery();
        airdropItemQuery.setAirdropId(ticket.getAirdropId());
        List<Account> accountList = airdropItemService.searchAccount(airdropItemQuery);
        List<AccountRef> accountRefList = accountList.stream().map(account -> {
           AccountRef accountRef = new AccountRef();
           BeanUtils.copyProperties(account, accountRef);
           return accountRef;
        }).toList();
        return RpcResult.success(new SyncDiamondAccountsToLocalResponse(accountRefList));
    }
}
