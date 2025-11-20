// WorkOrderIServiceImpl.java
package com.nest.diamond.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nest.diamond.iservice.WorkOrderIService;
import com.nest.diamond.mapper.WorkOrderMapper;
import com.nest.diamond.model.domain.Seed;
import com.nest.diamond.model.domain.WorkOrder;
import com.nest.diamond.model.domain.query.WorkOrderQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkOrderIServiceImpl extends ServiceImpl<WorkOrderMapper, WorkOrder>
        implements WorkOrderIService {

    @Override
    public List<WorkOrder> search(WorkOrderQuery query) {
        return baseMapper.search(query);
    }

    @Override
    public List<WorkOrder> findByAirdropOperationId(Long airdropOperationId) {
        LambdaQueryWrapper<WorkOrder> queryWrapper = new QueryWrapper<WorkOrder>().lambda();
        queryWrapper.eq(WorkOrder::getAirdropOperationId, airdropOperationId);
        return super.list(queryWrapper);
    }

}