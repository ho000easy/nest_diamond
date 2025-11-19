package com.nest.diamond.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.nest.diamond.common.enums.AccountType;
import com.nest.diamond.iservice.AccountIService;
import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.domain.query.AccountQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.Credentials;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountService {

    @Autowired
    private AccountIService accountIService;

    public void createCustodyAccount(String address) {
        Account _account = findByAddress(address);
        if (_account != null) {
            return;
        }
        Account account = new Account();
        account.setAddress(address);
        account.setType(AccountType.CUSTODY);
        accountIService.save(account);
    }

    public List<Account> batchBuildCustodyAccount(List<String> addressList) {
        List<Account> accountList = findByAddresses(addressList);
        Map<String, Account> accountMap = Maps.uniqueIndex(accountList, Account::getAddress);
        List<String> filteredAddressList = addressList.stream().filter(address -> !accountMap.containsKey(address)).collect(Collectors.toList());
        return filteredAddressList.stream().map(address -> {
            Account account = new Account();
            account.setAddress(address);
            account.setType(AccountType.CUSTODY);
            return account;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void updateBatch(List<Account> accountList) {
        accountIService.updateBatchById(accountList, 5000);
    }

    public void updateById(Account account) {
        accountIService.updateById(account);
    }

    public List<Account> search(AccountQuery accountQuery) {
        return accountIService.search(accountQuery);
    }

    public Page<Account> search(IPage<Account> iPage, AccountQuery accountQuery) {
        return accountIService.search(iPage, accountQuery);
    }


    public Account findById(Long id) {
        return accountIService.getById(id);
    }

    public List<Account> findAll() {
        return accountIService.findAll();
    }

    public List<Account> findAllNonCustody() {
        return accountIService.findAllNonCustody();
    }

    public List<Account> findByIds(List<Long> ids) {
        return accountIService.findByIds(ids);
    }

    public Account findByAddress(String address) {
        return accountIService.findByAddress(address);
    }


    public List<Account> findByAddresses(List<String> addresses) {
        return accountIService.findByAddresses(addresses);
    }

    public List<Account> findBySeedId(Long seedId) {
        return accountIService.findBySeedId(seedId);
    }

    public List<Account> findAccounts(Long seedId, Integer startHDIndex, Integer endHDIndex) {
        return accountIService.findAccounts(seedId, startHDIndex, endHDIndex);
    }

    public List<Account> findAccounts(Long seedId, List<Integer> hdIndexList) {
        return accountIService.findAccounts(seedId, hdIndexList);
    }

    public List<String> findAddresses(Long seedId, List<Integer> hdIndexList) {
        List<Account> accountList = findAccounts(seedId, hdIndexList);
        return accountList.stream().map(Account::getAddress).collect(Collectors.toList());
    }

    public List<String> findPrivateKeys(Long seedId, Integer startHDIndex, Integer endHDIndex) {
        List<Account> accountList = findAccounts(seedId, startHDIndex, endHDIndex);
        return accountList.stream().map(Account::getPrivateKey).collect(Collectors.toList());
    }

    public List<Credentials> findCredentialsList(Long seedId, Integer startHDIndex, Integer endHDIndex) {
        List<Account> accountList = findAccounts(seedId, startHDIndex, endHDIndex);
        return accountList.stream().map(account -> Credentials.create(account.getPrivateKey())).collect(Collectors.toList());
    }


    public List<String> findAddresses(Long seedId, Integer startHDIndex, Integer endHDIndex) {
        List<Account> accountList = findAccounts(seedId, startHDIndex, endHDIndex);
        return accountList.stream().map(Account::getAddress).collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(Long id) {
        accountIService.removeById(id);
    }

    @Transactional
    public void deleteByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return;
        }
        accountIService.removeBatchByIds(ids, 5000);
    }

    @Transactional
    public void batchInsert(List<Account> accountList) {
        accountIService.saveBatch(accountList, 5000);
    }
}
