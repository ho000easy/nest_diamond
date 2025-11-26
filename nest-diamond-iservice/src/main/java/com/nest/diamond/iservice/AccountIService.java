package com.nest.diamond.iservice;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.domain.query.AccountQuery;

import java.util.List;

public interface AccountIService extends IService<Account> {
    Account findByAddress(String address);
    List<Account> findByAddresses(List<String> addresses);


    List<Account> findBySeedId(Long seedId);
    List<Account> findByIds(List<Long> ids);

    List<Account> findAll();

    List<Account> findAllNonCustody();

    List<Account> findAccounts(Long seedId, Integer startHDIndex, Integer endHDIndex);

    List<Account> findAccounts(Long seedId, List<Integer> hdIndexList);

    Account selectNonCustodyOne();

    List<Account> search(AccountQuery accountQuery);

    Page<Account> search(IPage<Account> iPage, AccountQuery accountQuery);
}
