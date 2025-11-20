// ContractInstanceSnapshotIServiceImpl.java
package com.nest.diamond.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nest.diamond.iservice.ContractInstanceSnapshotIService;
import com.nest.diamond.mapper.ContractInstanceSnapshotMapper;
import com.nest.diamond.model.domain.ContractInstance;
import com.nest.diamond.model.domain.ContractInstanceSnapshot;
import com.nest.diamond.model.domain.query.ContractInstanceSnapshotQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractInstanceSnapshotIServiceImpl
        extends ServiceImpl<ContractInstanceSnapshotMapper, ContractInstanceSnapshot>
        implements ContractInstanceSnapshotIService {

    @Override
    public List<ContractInstanceSnapshot> search(ContractInstanceSnapshotQuery query) {
        return baseMapper.search(query);
    }

    @Override
    public ContractInstanceSnapshot findByChainIdAndContractAddress(String workOrderNo, Long chainId, String contractAddress) {
        LambdaQueryWrapper<ContractInstanceSnapshot> queryWrapper = new QueryWrapper<ContractInstanceSnapshot>().lambda();
        queryWrapper.eq(ContractInstanceSnapshot::getWorkOrderNo, workOrderNo);
        queryWrapper.eq(ContractInstanceSnapshot::getChainId, chainId);
        queryWrapper.eq(ContractInstanceSnapshot::getAddress, contractAddress);
        return super.getOne(queryWrapper);
    }

}