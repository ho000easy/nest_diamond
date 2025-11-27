package com.nest.diamond.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nest.diamond.iservice.TicketTokenIService;
import com.nest.diamond.mapper.TicketTokenMapper;
import com.nest.diamond.model.domain.Ticket;
import com.nest.diamond.model.domain.TicketToken;
import com.nest.diamond.model.domain.query.TicketTokenQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketTokenServiceImpl extends ServiceImpl<TicketTokenMapper, TicketToken> implements TicketTokenIService {

    @Override
    public List<TicketToken> search(TicketTokenQuery ticketTokenQuery) {
        return baseMapper.search(ticketTokenQuery);
    }

    @Override
    public TicketToken findByTicketNoAndToken(String ticketNo, String token) {
        LambdaQueryWrapper<TicketToken> queryWrapper = new QueryWrapper<TicketToken>().lambda();
        queryWrapper.in(TicketToken::getTicketNo, ticketNo);
        queryWrapper.in(TicketToken::getToken, token);
        return super.getOne(queryWrapper);
    }
}