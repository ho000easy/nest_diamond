package com.nest.diamond.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nest.diamond.model.domain.ContractInstance;
import com.nest.diamond.model.domain.query.ContractInstanceQuery;

import java.util.List;

public interface ContractInstanceMapper extends BaseMapper<ContractInstance> {
    List<ContractInstance> search(ContractInstanceQuery contractInstanceQuery);
}