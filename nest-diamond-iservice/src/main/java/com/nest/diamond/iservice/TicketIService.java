package com.nest.diamond.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nest.diamond.model.domain.Ticket;
import com.nest.diamond.model.domain.query.TicketQuery;

import java.util.List;

public interface TicketIService extends IService<Ticket> {
    List<Ticket> search(TicketQuery query);

    List<Ticket> findByTicketNos(List<String> ticketNoList);
    Ticket findByTicketNo(String ticketNo);
    Ticket findByName(String name);

    List<Ticket> findByAirdropOperationId(Long airdropOperationId);
}