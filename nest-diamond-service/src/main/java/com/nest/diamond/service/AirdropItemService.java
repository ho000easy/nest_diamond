package com.nest.diamond.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nest.diamond.iservice.AirdropItemIService;
import com.nest.diamond.model.bo.AirdropItemExtend;
import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.domain.Airdrop;
import com.nest.diamond.model.domain.AirdropItem;
import com.nest.diamond.model.domain.query.AirdropItemQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AirdropItemService {
    @Autowired
    private AirdropItemIService airdropItemIService;

    @Autowired
    private AirdropService airdropService;

    @Autowired
    private AccountService accountService;

    public AirdropItem findById(Long id) {
        return airdropItemIService.getById(id);
    }

    public List<AirdropItem> findByIds(List<Long> ids) {
        return airdropItemIService.listByIds(ids);
    }

    public List<AirdropItem> findByAirdropId(Long airdropId) {
        return airdropItemIService.findByAirdropId(airdropId);
    }


    public List<AirdropItem> findByAirdropIdAndSequence(Long airdropId, Integer startSequence, Integer endSequence) {

        return airdropItemIService.findByAirdropIdAndSequenceRange(airdropId, startSequence, endSequence);
    }

    public AirdropItem findByAirdropIdAndSequence(Long airdropId, Integer sequence) {
        return airdropItemIService.findByAirdropIdAndSequences(airdropId, Lists.newArrayList(sequence)).get(0);
    }

    public AirdropItem findByAirdropIdAndAddress(Long airdropId, String address) {
        List<AirdropItem> list = airdropItemIService.findByAirdropIdAndAddress(airdropId, address);
        return CollectionUtils.isNotEmpty(list) && list.size() > 0 ? list.get(0) : null;
    }

    public List<AirdropItem> findByAirdropIdAndSequence(Long airdropId, List<Integer> sequenceList) {
        return airdropItemIService.findByAirdropIdAndSequences(airdropId, sequenceList);
    }

    public List<AirdropItemExtend> search(AirdropItemQuery airdropItemQuery){
        return airdropItemIService.search(airdropItemQuery);
    }

    public List<Account> searchAccount(AirdropItemQuery airdropItemQuery){
        return airdropItemIService.searchAccount(airdropItemQuery);
    }

    public List<Account> findAccounts(Long airdropId, Integer startSequence, Integer endSequence){
        AirdropItemQuery airdropItemQuery = new AirdropItemQuery();
        airdropItemQuery.setAirdropId(airdropId);
        airdropItemQuery.setStartSequence(startSequence);
        airdropItemQuery.setEndSequence(endSequence);
        return searchAccount(airdropItemQuery);
    }

    public Account findBy(Long airdropId, Integer sequence){
        AirdropItemQuery airdropItemQuery = new AirdropItemQuery();
        airdropItemQuery.setAirdropId(airdropId);
        airdropItemQuery.setStartSequence(sequence);
        airdropItemQuery.setEndSequence(sequence);
        List<Account> accountList = searchAccount(airdropItemQuery);
        return accountList.isEmpty() ? null : accountList.get(0);
    }


    public List<AirdropItem> findByAirdropIdAndAccountIds(Long airdropId, List<Long> accountIds) {
        return airdropItemIService.selectByAirdropIdAndAccountIds(airdropId, accountIds);
    }

    public void batchCreateAirdropItem(String airdropName, List<String> addressList) {

        Airdrop airdrop = airdropService.findByName(airdropName);
        List<AirdropItem> __airdropItemList = findByAirdropId(airdrop.getId());
        Map<String, AirdropItem> __airdropItemMap = Maps.uniqueIndex(__airdropItemList, AirdropItem::getAccountAddress);
        List<String> _addressList = addressList.stream().filter(address->{
            return !__airdropItemMap.containsKey(address);
        }).collect(Collectors.toList());

        // 使用map解决in查询的无序问题
        List<Account> accountList = accountService.findByAddresses(_addressList);
        Map<String, Account> accountMap = Maps.uniqueIndex(accountList, Account::getAddress);

        AirdropItem maxSequenceItem = airdropItemIService.findMaxSequenceItem(airdrop.getId());
        int startSequence = maxSequenceItem == null ? 1 : maxSequenceItem.getSequence() + 1;
        List<AirdropItem> airdropItemList = Lists.newArrayList();
        for (int i = startSequence; i < startSequence + _addressList.size(); i++) {
            Account account = accountMap.get(_addressList.get(i - startSequence));
            AirdropItem airdropItem = new AirdropItem();
            airdropItem.setAirdropId(airdrop.getId());
            airdropItem.setAirdropName(airdrop.getName());
            airdropItem.setSequence(i);
            airdropItem.setAccountAddress(account.getAddress());
            airdropItem.setAccountId(account.getId());
            airdropItemList.add(airdropItem);
        }
        insertBatch(airdropItemList);
    }

    @Transactional
    public void insertBatch(List<AirdropItem> airdropItemList){
        airdropItemIService.saveBatch(airdropItemList, 5000);
    }

    public void batchUpdate(List<AirdropItem> airdropItemList){
        airdropItemIService.updateBatchById(airdropItemList, 5000);
    }

    public void deleteByAirdropId(Long airdropId){
        List<AirdropItem> airdropItemList = findByAirdropId(airdropId);
        if(airdropItemList.isEmpty()){
            return;
        }
        airdropItemIService.deleteByAirdropId(airdropId);
    }

    public void deleteByIds(List<Long> ids){
        airdropItemIService.removeBatchByIds(ids, 5000);
    }
}
