package com.nest.diamond.service;

import com.google.common.collect.Lists;
import com.nest.diamond.common.enums.MethodIdVendor;
import com.nest.diamond.iservice.ContractInstanceFunctionIService;
import com.nest.diamond.model.domain.ContractInstanceFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ContractInstanceFunctionService {

    @Autowired
    private ContractInstanceFunctionIService contractInstanceFunctionIService;

    public Boolean save(ContractInstanceFunction function){
       return contractInstanceFunctionIService.save(function);
    }

    public List<ContractInstanceFunction> findByMethodIdVendor(MethodIdVendor methodIdVendor){
        return contractInstanceFunctionIService.findByMethodIdVendor(methodIdVendor);
    }

    public List<String> findMethIds(MethodIdVendor methodIdVendor){
        List<ContractInstanceFunction> list = findByMethodIdVendor(methodIdVendor);
        List<String> methIds = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(list)){
            list.forEach( function ->{
                methIds.add(function.getFunctionMethodId());
            });
        }
        return methIds;
    }
}
