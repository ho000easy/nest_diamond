package com.nest.diamond.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.nest.diamond.common.util.ListUtil;
import com.nest.diamond.iservice.AirdropItemIService;
import com.nest.diamond.mapper.AirdropItemMapper;
import com.nest.diamond.model.bo.AirdropItemExtend;
import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.domain.AirdropItem;
import com.nest.diamond.model.domain.query.AirdropItemQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirdropItemIServiceImpl extends ServiceImpl<AirdropItemMapper, AirdropItem> implements AirdropItemIService {

    @Override
    public List<AirdropItem> findByAirdropId(Long airdropId) {
        LambdaQueryWrapper<AirdropItem> queryWrapper = new QueryWrapper<AirdropItem>().lambda();
        queryWrapper.eq(AirdropItem::getAirdropId, airdropId);
        return super.list(queryWrapper);
    }

    @Override
    public List<AirdropItem> findByAirdropIdAndSequenceRange(Long airdropId, Integer startSequence, Integer endSequence) {
        LambdaQueryWrapper<AirdropItem> queryWrapper = new QueryWrapper<AirdropItem>().lambda();
        queryWrapper.eq(AirdropItem::getAirdropId, airdropId);
        queryWrapper.ge(AirdropItem::getSequence, startSequence);
        queryWrapper.le(AirdropItem::getSequence, endSequence);
        queryWrapper.orderByAsc(AirdropItem::getSequence);
        return super.list(queryWrapper);
    }

    @Override
    public List<AirdropItem> findByAirdropIdAndSequences(Long airdropId, List<Integer> sequenceList) {
        LambdaQueryWrapper<AirdropItem> queryWrapper = new QueryWrapper<AirdropItem>().lambda();
        queryWrapper.eq(AirdropItem::getAirdropId, airdropId);
        queryWrapper.in(AirdropItem::getSequence, sequenceList);
        queryWrapper.orderByAsc(AirdropItem::getSequence);
        return super.list(queryWrapper);
    }

    @Override
    public List<AirdropItem> findByAirdropIdAndAddress(Long airdropId, String address) {
        LambdaQueryWrapper<AirdropItem> queryWrapper = new QueryWrapper<AirdropItem>().lambda();
        queryWrapper.eq(AirdropItem::getAirdropId, airdropId);
        queryWrapper.in(AirdropItem::getAccountAddress, address);
        return super.list(queryWrapper);
    }

    @Override
    public AirdropItem findMaxSequenceItem(Long airdropId) {
        LambdaQueryWrapper<AirdropItem> queryWrapper = new QueryWrapper<AirdropItem>().lambda();
        queryWrapper.eq(AirdropItem::getAirdropId, airdropId);
        queryWrapper.orderByDesc(AirdropItem::getSequence);
        queryWrapper.last("limit 1");
        return super.getOne(queryWrapper);
    }

    @Override
    public void deleteByAirdropId(Long airdropId) {
        LambdaQueryWrapper<AirdropItem> queryWrapper = new QueryWrapper<AirdropItem>().lambda();
        queryWrapper.eq(AirdropItem::getAirdropId, airdropId);
        super.remove(queryWrapper);
    }

    @Override
    public List<AirdropItemExtend> search(AirdropItemQuery airdropItemQuery) {
        return baseMapper.search(airdropItemQuery);
    }

    @Override
    public List<Account> searchAccount(AirdropItemQuery airdropItemQuery) {
        return baseMapper.searchAccount(airdropItemQuery);

    }

    @Override
    public List<AirdropItem> selectByAirdropIdAndAccountIds(Long airdropId, List<Long> accountIds) {
        List<List<Long>> partionList = ListUtil.partitionList(accountIds, 5000);
        List<AirdropItem> airdropItemList = Lists.newArrayList();
        for(List<Long> partition : partionList){
            airdropItemList.addAll(baseMapper.selectByAirdropIdAndAccountIds(airdropId, partition));
        }
        return airdropItemList;
    }
}
