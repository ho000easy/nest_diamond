package com.nest.diamond.iservice;


import com.baomidou.mybatisplus.extension.service.IService;
import com.nest.diamond.model.bo.AirdropItemExtend;
import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.domain.AirdropItem;
import com.nest.diamond.model.domain.query.AirdropItemQuery;

import java.util.List;

public interface AirdropItemIService extends IService<AirdropItem> {

    List<AirdropItem> findByAirdropId(Long airdropId);

    List<AirdropItem> findByAirdropIdAndSequenceRange(Long airdropId, Integer startSequence, Integer endSequence);

    List<AirdropItem> findByAirdropIdAndSequences(Long airdropId, List<Integer> sequenceList);

    List<AirdropItem> findByAirdropIdAndAddress(Long airdropId, String address);

    AirdropItem findMaxSequenceItem(Long airdropId);

    void deleteByAirdropId(Long airdropId);

    List<AirdropItemExtend> search(AirdropItemQuery airdropItemQuery);

    List<Account> searchAccount(AirdropItemQuery airdropItemQuery);

    List<AirdropItem> selectByAirdropIdAndAccountIds(Long airdropId, List<Long> accountIds);
}
