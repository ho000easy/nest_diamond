package com.nest.diamond.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nest.diamond.model.domain.Ticket;
import com.nest.diamond.model.domain.query.TicketQuery;

import java.util.List;

public interface TicketIService extends IService<Ticket> {
    List<Ticket> search(TicketQuery query);
}