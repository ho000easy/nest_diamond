package com.nest.diamond.service;

import com.nest.diamond.iservice.ProtocolIService;
import com.nest.diamond.model.domain.Contract;
import com.nest.diamond.model.domain.Protocol;
import com.nest.diamond.model.domain.query.ProtocolQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProtocolService {

    @Autowired
    private ProtocolIService protocolIService;

    @Autowired
    private ContractService contractService;


    public List<Protocol> allProtocols(){
        return protocolIService.allProtocols();
    }

    public void insert(Protocol protocol){
        protocolIService.save(protocol);
    }

    @Transactional
    public void update(Protocol protocol){
        Protocol _protocol = protocolIService.getById(protocol.getId());
        _protocol.setName(protocol.getName());
        _protocol.setDiscord(protocol.getDiscord());
        _protocol.setGithub(protocol.getGithub());
        _protocol.setTwitter(protocol.getTwitter());
        protocolIService.updateById(_protocol);

        List<Contract> contractList = contractService.findByProtocolId(protocol.getId());
        contractList.forEach(contract -> {
            contractService.updateProtocolName(contract.getId(), protocol.getName());
        });
    }


    public Protocol findBy(String name){
        return protocolIService.findBy(name);
    }

    public List<Protocol> search(ProtocolQuery protocolQuery) {
        return protocolIService.search(protocolQuery);
    }

    public Protocol findById(Long id){
        return protocolIService.getById(id);
    }

    @Transactional
    public void deleteById(Long id){
        List<Contract> contracts = contractService.findByProtocolId(id);
        List<Long> contractIds = contracts.stream().mapToLong(Contract::getId).boxed().collect(Collectors.toList());
        contractService.deleteByIds(contractIds);

        protocolIService.removeById(id);
    }

    @Transactional
    public void deleteByIds(List<Long> ids){
        ids.forEach(id -> this.deleteById(id));
    }
}
