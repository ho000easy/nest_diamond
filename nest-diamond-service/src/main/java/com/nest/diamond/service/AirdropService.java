package com.nest.diamond.service;

import com.nest.diamond.iservice.AirdropIService;
import com.nest.diamond.model.bo.AirdropItemExtend;
import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.domain.Airdrop;
import com.nest.diamond.model.domain.query.AirdropItemQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AirdropService {

    @Autowired
    private AirdropIService airdropIService;

    @Autowired
    private AirdropItemService airdropItemService;

    public List<Account> findAccountsByAirdropId(Long id, Integer startSequence, Integer endSequence){
        AirdropItemQuery airdropItemQuery = new AirdropItemQuery();
        airdropItemQuery.setAirdropId(id);
        airdropItemQuery.setStartSequence(startSequence);
        airdropItemQuery.setEndSequence(endSequence);
        return airdropItemService.searchAccount(airdropItemQuery);
    }

    public List<Account> findAccounts(Long id, Integer startSequence, Integer endSequence, List<Integer> sequenceList) {
        return (startSequence != null && endSequence != null)
                ? findAccountsByAirdropId(id, startSequence, endSequence)
                : findAccounts(id, sequenceList);
    }

    public List<Account> findAccounts(Long id, List<Integer> sequenceList){
        AirdropItemQuery airdropItemQuery = new AirdropItemQuery();
        airdropItemQuery.setAirdropId(id);
        airdropItemQuery.setSequenceList(sequenceList);
        return airdropItemService.searchAccount(airdropItemQuery);
    }

    public List<Account> findAccountsByAirdropId(Long id){
        AirdropItemQuery airdropItemQuery = new AirdropItemQuery();
        airdropItemQuery.setAirdropId(id);
        return airdropItemService.searchAccount(airdropItemQuery);
    }

    public Account findAccountByAirdropId(Long id, Integer sequence){
        AirdropItemQuery airdropItemQuery = new AirdropItemQuery();
        airdropItemQuery.setAirdropId(id);
        airdropItemQuery.setStartSequence(sequence);
        airdropItemQuery.setEndSequence(sequence);
        List<Account> accountList = airdropItemService.searchAccount(airdropItemQuery);
        return accountList.isEmpty() ? null : accountList.get(0);
    }

    public List<String> findAddressByAirdropId(Long id, Integer startSequence, Integer endSequence) {
        return findAddressByAirdropId(id, startSequence, endSequence, null);
    }

    public List<String> findAddressByAirdropId(Long id, Integer startSequence, Integer endSequence, List<Integer> sequenceList){
        List<AirdropItemExtend> airdropItemExtends = findAirdropItemExtendByAirdropId(id, startSequence, endSequence, sequenceList);
        return airdropItemExtends.stream().map(AirdropItemExtend::getAccountAddress).collect(Collectors.toList());
    }

    public List<AirdropItemExtend> findAirdropItemExtendByAirdropId(Long id, Integer startSequence, Integer endSequence, List<Integer> sequenceList){
        AirdropItemQuery airdropItemQuery = new AirdropItemQuery();
        airdropItemQuery.setAirdropId(id);
        airdropItemQuery.setStartSequence(startSequence);
        airdropItemQuery.setEndSequence(endSequence);
        airdropItemQuery.setSequenceList(sequenceList);

        return airdropItemService.search(airdropItemQuery);
    }

    public void insert(Airdrop airdrop) {
        airdropIService.save(airdrop);
    }


    public Airdrop findByName(String name) {
        return airdropIService.getByName(name);
    }

    @Transactional
    public void deleteByIds(List<Long> ids){
        ids.forEach(id -> deleteById(id));
    }

    public void deleteById(Long id){
        airdropItemService.deleteByAirdropId(id);
        airdropIService.removeById(id);
    }


    public List<Airdrop> allAirdrops() {
        return airdropIService.allAirdrops();
    }

    public List<Airdrop> search(String name) {
        return airdropIService.search(name);
    }

    public Airdrop findById(Long id){
        return airdropIService.getById(id);
    }

    public List<Airdrop> findByIds(List<Long> ids){
        return airdropIService.findByIds(ids);
    }


    public void createAirdrop(String airdropName, List<String> accountAddressList) {
        createAirdrop(airdropName, accountAddressList, null);
    }
    @Transactional
    public void createAirdrop(String airdropName, List<String> accountAddressList, String remark) {
        Airdrop airdrop = new Airdrop();
        airdrop.setName(airdropName);
        airdrop.setRemark(remark);
        airdropIService.save(airdrop);

        if(accountAddressList.isEmpty()){
            return;
        }
        airdropItemService.batchCreateAirdropItem(airdropName, accountAddressList);
    }

    public void update(Airdrop airdrop){
        Airdrop _airdrop = findById(airdrop.getId());
        _airdrop.setRemark(airdrop.getRemark());
        _airdrop.setName(airdrop.getName());
        airdropIService.updateById(_airdrop);
    }



}
