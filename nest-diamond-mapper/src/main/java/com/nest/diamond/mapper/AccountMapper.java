package com.nest.diamond.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.domain.query.AccountQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountMapper extends BaseMapper<Account> {
    List<Account> search(AccountQuery accountQuery);
    Page<Account> search(IPage<Account> iPage, @Param("condition") AccountQuery accountQuery);

}