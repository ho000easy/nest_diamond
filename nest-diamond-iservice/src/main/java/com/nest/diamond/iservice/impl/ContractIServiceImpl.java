
package com.nest.diamond.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nest.diamond.iservice.ContractIService;
import com.nest.diamond.mapper.ContractMapper;
import com.nest.diamond.model.domain.Contract;
import com.nest.diamond.model.domain.query.ContractQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractIServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractIService {
    @Override
    public List<Contract> findBy(Long protocolId) {
        LambdaQueryWrapper<Contract> queryWrapper = new QueryWrapper<Contract>().lambda();
        queryWrapper.eq(Contract::getProtocolId, protocolId);
        return super.list(queryWrapper);
    }

    @Override
    public List<Contract> search(ContractQuery contractQuery) {
        return baseMapper.search(contractQuery);
    }

}
