package com.nest.diamond.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nest.diamond.iservice.TicketIService;
import com.nest.diamond.mapper.TicketMapper;
import com.nest.diamond.model.domain.Ticket;
import com.nest.diamond.model.domain.query.TicketQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketIServiceImpl extends ServiceImpl<TicketMapper, Ticket>
        implements TicketIService {

    @Override
    public List<Ticket> search(TicketQuery query) {
        return baseMapper.search(query);
    }

    @Override
    public List<Ticket> findByTicketNos(List<String> ticketNoList) {
        LambdaQueryWrapper<Ticket> queryWrapper = new QueryWrapper<Ticket>().lambda();
        queryWrapper.in(Ticket::getTicketNo, ticketNoList);
        return super.list(queryWrapper);
    }

    @Override
    public Ticket findByTicketNo(String ticketNo) {
        LambdaQueryWrapper<Ticket> queryWrapper = new QueryWrapper<Ticket>().lambda();
        queryWrapper.eq(Ticket::getTicketNo, ticketNo);
        return super.getOne(queryWrapper);
    }

    @Override
    public Ticket findByName(String name) {
        LambdaQueryWrapper<Ticket> queryWrapper = new QueryWrapper<Ticket>().lambda();
        queryWrapper.eq(Ticket::getName, name);
        return super.getOne(queryWrapper);
    }

    @Override
    public List<Ticket> findByAirdropOperationId(Long airdropOperationId) {
        LambdaQueryWrapper<Ticket> queryWrapper = new QueryWrapper<Ticket>().lambda();
        queryWrapper.eq(Ticket::getAirdropOperationId, airdropOperationId);
        return super.list(queryWrapper);
    }

}