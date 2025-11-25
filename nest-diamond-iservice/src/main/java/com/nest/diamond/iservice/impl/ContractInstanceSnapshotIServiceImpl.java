// ContractInstanceSnapshotIServiceImpl.java
package com.nest.diamond.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nest.diamond.iservice.ContractInstanceSnapshotIService;
import com.nest.diamond.mapper.ContractInstanceSnapshotMapper;
import com.nest.diamond.model.domain.ContractInstanceSnapshot;
import com.nest.diamond.model.domain.query.ContractInstanceSnapshotQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractInstanceSnapshotIServiceImpl
        extends ServiceImpl<ContractInstanceSnapshotMapper, ContractInstanceSnapshot>
        implements ContractInstanceSnapshotIService {

    @Override
    public List<ContractInstanceSnapshot> search(ContractInstanceSnapshotQuery query) {
        return baseMapper.search(query);
    }

    @Override
    public ContractInstanceSnapshot findBy(String ticketNo, Long chainId, String contractAddress) {
        LambdaQueryWrapper<ContractInstanceSnapshot> queryWrapper = new QueryWrapper<ContractInstanceSnapshot>().lambda();
        queryWrapper.eq(ContractInstanceSnapshot::getTicketNo, ticketNo);
        queryWrapper.eq(ContractInstanceSnapshot::getChainId, chainId);
        queryWrapper.eq(ContractInstanceSnapshot::getAddress, contractAddress);
        return super.getOne(queryWrapper);
    }

}