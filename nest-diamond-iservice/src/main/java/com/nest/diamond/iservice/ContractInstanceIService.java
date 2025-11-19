package com.nest.diamond.iservice;


import com.baomidou.mybatisplus.extension.service.IService;
import com.nest.diamond.model.domain.ContractInstance;
import com.nest.diamond.model.domain.query.ContractInstanceQuery;

import java.util.List;

public interface ContractInstanceIService extends IService<ContractInstance> {
    List<ContractInstance> findBy(Long contractId);
    ContractInstance findBy(Long chainId, String contractName);
    List<ContractInstance> findByFuzzyContractName(Long chainId, String fuzzyContractName);
    ContractInstance findBy(String chainName, String contractName);

    ContractInstance findByChainIdAndContractAddress(Long chainId, String contractAddress);
    ContractInstance findByChainNameAndContractAddress(String chainName, String contractAddress);

    List<ContractInstance> search(ContractInstanceQuery contractInstanceQuery);
}
