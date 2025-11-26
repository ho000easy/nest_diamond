package com.nest.diamond.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nest.diamond.common.enums.AccountType;
import com.nest.diamond.common.enums.WalletVendor;
import com.nest.diamond.iservice.AccountIService;
import com.nest.diamond.mapper.AccountMapper;
import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.domain.query.AccountQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountIServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountIService {

    @Override
    public Account findByAddress(String address) {
        LambdaQueryWrapper<Account> queryWrapper = new QueryWrapper<Account>().lambda();
        queryWrapper.eq(Account::getAddress, address);
        return getOne(queryWrapper);
    }

    @Override
    public List<Account> findByAddresses(List<String> addresses) {
        LambdaQueryWrapper<Account> queryWrapper = new QueryWrapper<Account>().lambda();
        queryWrapper.in(Account::getAddress, addresses);
        return list(queryWrapper);
    }

    @Override
    public List<Account> findBySeedId(Long seedId) {
        LambdaQueryWrapper<Account> queryWrapper = new QueryWrapper<Account>().lambda();
        queryWrapper.in(Account::getSeedId, seedId);
        return list(queryWrapper);
    }

    @Override
    public List<Account> findByIds(List<Long> ids) {
        LambdaQueryWrapper<Account> queryWrapper = new QueryWrapper<Account>().lambda();
        queryWrapper.in(Account::getId, ids);
        return list(queryWrapper);
    }


    @Override
    public List<Account> findAll() {
        LambdaQueryWrapper<Account> queryWrapper = new QueryWrapper<Account>().lambda();
        return list(queryWrapper);
    }

    @Override
    public List<Account> findAllNonCustody() {
        LambdaQueryWrapper<Account> queryWrapper = new QueryWrapper<Account>().lambda();
        queryWrapper.eq(Account::getType, AccountType.NON_CUSTODY);
        return list(queryWrapper);
    }

    @Override
    public List<Account> findAccounts(Long seedId, Integer startHDIndex, Integer endHDIndex) {
        LambdaQueryWrapper<Account> queryWrapper = new QueryWrapper<Account>().lambda();
        queryWrapper.eq(Account::getSeedId, seedId);
        queryWrapper.between(Account::getHdIndex, startHDIndex, endHDIndex);
        queryWrapper.orderByAsc(Account::getHdIndex);
        return list(queryWrapper);

    }


    @Override
    public List<Account> findAccounts(Long seedId, List<Integer> hdIndexList) {
        LambdaQueryWrapper<Account> queryWrapper = new QueryWrapper<Account>().lambda();
        queryWrapper.eq(Account::getSeedId, seedId);
        queryWrapper.in(Account::getHdIndex, hdIndexList);
        return list(queryWrapper);
    }

    @Override
    public Account selectNonCustodyOne() {
        LambdaQueryWrapper<Account> queryWrapper = new QueryWrapper<Account>().lambda();
        queryWrapper.eq(Account::getType, AccountType.NON_CUSTODY);
        queryWrapper.eq(Account::getWalletVendor, WalletVendor.METAMASK);
        queryWrapper.last("order by id desc limit 1");
        return getOne(queryWrapper);
    }

    @Override
    public List<Account> search(AccountQuery accountQuery) {
        return baseMapper.search(accountQuery);
    }

    @Override
    public Page<Account> search(IPage<Account> iPage, AccountQuery accountQuery) {
        return baseMapper.search(iPage, accountQuery);
    }

}
