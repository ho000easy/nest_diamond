package com.nest.diamond.service;

import com.nest.diamond.iservice.ContractInstanceIService;
import com.nest.diamond.model.domain.BlockChain;
import com.nest.diamond.model.domain.Contract;
import com.nest.diamond.model.domain.ContractInstance;
import com.nest.diamond.model.domain.Protocol;
import com.nest.diamond.model.domain.query.ContractInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractInstanceService {
    @Autowired
    private ContractInstanceIService contractInstanceIService;
    @Autowired
    private ProtocolService protocolService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private BlockchainService blockchainService;


    public List<ContractInstance> findByContractId(Long contractId){
        return contractInstanceIService.findBy(contractId);
    }

    public ContractInstance findById(Long id){
        return contractInstanceIService.getById(id);
    }

    public List<ContractInstance> findByIds(List<Long> ids){
        return contractInstanceIService.listByIds(ids);
    }

    public ContractInstance findBy(Long chainId, String contractName){
        return contractInstanceIService.findBy(chainId, contractName);
    }

    public ContractInstance findBy(String chainName, String contractName){
        return contractInstanceIService.findBy(chainName, contractName);
    }

    public List<ContractInstance> findByFuzzyContractName(Long chainId, String fuzzyContractName){
        return contractInstanceIService.findByFuzzyContractName(chainId, fuzzyContractName);
    }

    public ContractInstance findByChainIdAndContractAddress(Long chainId, String contractAddress){
        return contractInstanceIService.findByChainIdAndContractAddress(chainId, contractAddress);
    }

    public ContractInstance findByChainNameAndContractAddress(String chainName, String contractAddress){
        return contractInstanceIService.findByChainNameAndContractAddress(chainName, contractAddress);
    }

    public void create(ContractInstance contractInstance){
        Protocol protocol = protocolService.findById(contractInstance.getProtocolId());
        Contract contract = contractService.findById(contractInstance.getContractId());
        BlockChain blockChain  = blockchainService.findByChainId(contractInstance.getChainId());
        contractInstance.setProtocolName(protocol.getName());
        contractInstance.setContractName(contract.getName());
        contractInstance.setChainName(blockChain.getChainName());
        contractInstanceIService.save(contractInstance);
    }

    public void updateById(ContractInstance contractInstance){
        contractInstanceIService.updateById(contractInstance);
    }

    public void insert(ContractInstance contractInstance){
        contractInstanceIService.save(contractInstance);
    }

    public void deleteById(Long id){
        contractInstanceIService.removeById(id);
    }

    public void deleteByIds(List<Long> ids){
        contractInstanceIService.removeBatchByIds(ids);
    }

    public List<ContractInstance> search(ContractInstanceQuery contractInstanceQuery){
        return contractInstanceIService.search(contractInstanceQuery);
    }

}
