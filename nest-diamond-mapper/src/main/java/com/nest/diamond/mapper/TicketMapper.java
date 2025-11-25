package com.nest.diamond.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nest.diamond.model.domain.Ticket;
import com.nest.diamond.model.domain.query.TicketQuery;

import java.util.List;

public interface TicketMapper extends BaseMapper<Ticket> {
    List<Ticket> search(TicketQuery query);
}