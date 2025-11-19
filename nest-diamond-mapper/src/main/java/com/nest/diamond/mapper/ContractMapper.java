package com.nest.diamond.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nest.diamond.model.domain.Contract;
import com.nest.diamond.model.domain.query.ContractQuery;

import java.util.List;

public interface ContractMapper extends BaseMapper<Contract> {
    List<Contract> search(ContractQuery contractQuery);
}