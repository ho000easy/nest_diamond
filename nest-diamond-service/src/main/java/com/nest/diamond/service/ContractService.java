package com.nest.diamond.service;

import com.nest.diamond.iservice.ContractIService;
import com.nest.diamond.model.domain.Contract;
import com.nest.diamond.model.domain.ContractInstance;
import com.nest.diamond.model.domain.Protocol;
import com.nest.diamond.model.domain.query.ContractQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractService {
    @Autowired
    private ContractIService contractIService;

    @Autowired
    private ContractInstanceService contractInstanceService;

    @Autowired
    private ProtocolService protocolService;

    public Contract findById(Long id){
        return contractIService.getById(id);
    }
    public List<Contract> findByProtocolId(Long protocolId) {
        return contractIService.findBy(protocolId);
    }

    public List<Contract> findByProtocolName(String protocolName) {
        return contractIService.findByProtocolName(protocolName);
    }

    public void create(Contract contract) {
        Protocol protocol = protocolService.findById(contract.getProtocolId());
        contract.setProtocolName(protocol.getName());
        contractIService.save(contract);
    }

    public void deleteById(Long id) {
        List<ContractInstance> contractInstanceList = contractInstanceService.findByContractId(id);
        List<Long> contractInstanceIdList = contractInstanceList.stream().mapToLong(ContractInstance::getId).boxed().collect(Collectors.toList());
        contractInstanceService.deleteByIds(contractInstanceIdList);
        contractIService.removeById(id);
    }

    public void deleteByIds(List<Long> ids) {
        ids.forEach(id -> this.deleteById(id));
    }

    public void insert(Contract contract){
        contractIService.save(contract);
    }

    @Transactional
    public void updateProtocolName(Long id, String protocolName){
        Contract contract = findById(id);
        contract.setProtocolName(protocolName);
        contractIService.updateById(contract);
        List<ContractInstance> contractInstanceList = contractInstanceService.findByContractId(id);
        contractInstanceList.forEach(contractInstance -> {
            contractInstance.setProtocolName(protocolName);
            contractInstanceService.updateById(contractInstance);
        });
    }

    public List<Contract> search(ContractQuery contractQuery) {
        return contractIService.search(contractQuery);
    }
}
