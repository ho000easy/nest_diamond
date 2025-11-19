

package com.nest.diamond.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nest.diamond.common.enums.MethodIdVendor;
import com.nest.diamond.iservice.ContractInstanceFunctionIService;
import com.nest.diamond.mapper.ContractInstanceFunctionMapper;
import com.nest.diamond.model.domain.ContractInstanceFunction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractInstanceFunctionIServiceImpl extends ServiceImpl<ContractInstanceFunctionMapper, ContractInstanceFunction> implements ContractInstanceFunctionIService {

    @Override
    public List<ContractInstanceFunction> findByMethodIdVendor(MethodIdVendor methodIdVendor) {
        LambdaQueryWrapper<ContractInstanceFunction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractInstanceFunction :: getFunctionMethodType, methodIdVendor);
        return list(queryWrapper);
    }
}
