
package com.nest.diamond.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nest.diamond.iservice.ContractInstanceIService;
import com.nest.diamond.mapper.ContractInstanceMapper;
import com.nest.diamond.model.domain.ContractInstance;
import com.nest.diamond.model.domain.query.ContractInstanceQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractInstanceIServiceImpl extends ServiceImpl<ContractInstanceMapper, ContractInstance> implements ContractInstanceIService {

    @Override
    public List<ContractInstance> findBy(Long contractId) {
        LambdaQueryWrapper<ContractInstance> queryWrapper = new QueryWrapper<ContractInstance>().lambda();
        queryWrapper.eq(ContractInstance::getContractId, contractId);
        return super.list(queryWrapper);
    }

    @Override
    public ContractInstance findBy(Long chainId, String contractName) {
        LambdaQueryWrapper<ContractInstance> queryWrapper = new QueryWrapper<ContractInstance>().lambda();
        queryWrapper.eq(ContractInstance::getChainId, chainId);
        queryWrapper.eq(ContractInstance::getContractName, contractName);
        return super.getOne(queryWrapper);
    }

    @Override
    public List<ContractInstance> findByFuzzyContractName(Long chainId, String fuzzyContractName) {
        LambdaQueryWrapper<ContractInstance> queryWrapper = new QueryWrapper<ContractInstance>().lambda();
        queryWrapper.eq(ContractInstance::getChainId, chainId);
        queryWrapper.like(ContractInstance::getContractName, fuzzyContractName);
        return super.list(queryWrapper);
    }

    @Override
    public ContractInstance findBy(String chainName, String contractName) {
        LambdaQueryWrapper<ContractInstance> queryWrapper = new QueryWrapper<ContractInstance>().lambda();
        queryWrapper.eq(ContractInstance::getChainName, chainName);
        queryWrapper.eq(ContractInstance::getContractName, contractName);
        return super.getOne(queryWrapper);
    }

    @Override
    public ContractInstance findByChainIdAndContractAddress(Long chainId, String contractAddress) {
        LambdaQueryWrapper<ContractInstance> queryWrapper = new QueryWrapper<ContractInstance>().lambda();
        queryWrapper.eq(ContractInstance::getChainId, chainId);
        queryWrapper.eq(ContractInstance::getAddress, contractAddress);
        return super.getOne(queryWrapper);
    }

    @Override
    public ContractInstance findByChainNameAndContractAddress(String chainName, String contractAddress) {
        LambdaQueryWrapper<ContractInstance> queryWrapper = new QueryWrapper<ContractInstance>().lambda();
        queryWrapper.eq(ContractInstance::getChainName, chainName);
        queryWrapper.eq(ContractInstance::getAddress, contractAddress);
        return super.getOne(queryWrapper);
    }

    @Override
    public List<ContractInstance> search(ContractInstanceQuery contractInstanceQuery) {
        return baseMapper.search(contractInstanceQuery);

    }

}
