package com.nest.diamond.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nest.diamond.model.domain.TicketToken;
import com.nest.diamond.model.domain.query.TicketTokenQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TicketTokenMapper extends BaseMapper<TicketToken> {
    List<TicketToken> search(TicketTokenQuery ticketTokenQuery);
}