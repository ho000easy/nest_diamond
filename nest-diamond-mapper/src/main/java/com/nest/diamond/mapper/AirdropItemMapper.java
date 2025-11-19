package com.nest.diamond.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nest.diamond.model.bo.AirdropItemExtend;
import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.domain.AirdropItem;
import com.nest.diamond.model.domain.query.AirdropItemQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AirdropItemMapper extends BaseMapper<AirdropItem> {
    List<AirdropItemExtend> search(AirdropItemQuery airdropItemQuery);
    List<Account> searchAccount(AirdropItemQuery airdropItemQuery);
    List<AirdropItem> selectByAirdropIdAndAccountIds(@Param("airdropId") Long airdropId, @Param("accountIds") List<Long> accountIds);
}