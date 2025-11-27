package com.nest.diamond.service;

import com.nest.diamond.common.enums.TicketTokenStatusEnum;
import com.nest.diamond.iservice.TicketTokenIService;
import com.nest.diamond.model.domain.Ticket;
import com.nest.diamond.model.domain.TicketToken;
import com.nest.diamond.model.domain.query.TicketTokenQuery;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TicketTokenService {
    @Autowired
    private TicketTokenIService ticketTokenIService;
    @Autowired
    private TicketService ticketService;

    public List<TicketToken> search(TicketTokenQuery ticketTokenQuery){
        return ticketTokenIService.search(ticketTokenQuery);
    }

    public TicketToken findByTicketNoAndToken(String ticketNo, String token){
        return ticketTokenIService.findByTicketNoAndToken(ticketNo, token);
    }

    public TicketToken generateTicketToken(Long ticketId, int validSeconds) {
        Assert.notNull(ticketId, "工单ID不能为空");

        // 1. 生成随机 Token (移除横杠)
        String tokenStr = UUID.randomUUID().toString().replace("-", "");
        Ticket ticket = ticketService.findById(ticketId);
        // 2. 构建对象
        TicketToken ticketToken = new TicketToken();
        ticketToken.setTicketId(ticketId);
        ticketToken.setTicketNo(ticket.getTicketNo());
        ticketToken.setToken(tokenStr);
        Date expireDate = DateUtils.addSeconds(new Date(), validSeconds);
        ticketToken.setExpireTime(expireDate);
        ticketToken.setStatus(TicketTokenStatusEnum.UNUSED);

        // 3. 存入数据库
        ticketTokenIService.save(ticketToken);
        return ticketToken;
    }

    public void invalidate(List<Long> ids){
        List<TicketToken> ticketTokenList = ticketTokenIService.listByIds(ids);
        ticketTokenList.forEach(ticketToken -> {
            ticketToken.setStatus(TicketTokenStatusEnum.EXPIRED);
        });
        ticketTokenIService.updateBatchById(ticketTokenList, 5000);
    }

    public void deleteByIds(List<Long> ids){
        ticketTokenIService.removeBatchByIds(ids, 5000);
    }
}
