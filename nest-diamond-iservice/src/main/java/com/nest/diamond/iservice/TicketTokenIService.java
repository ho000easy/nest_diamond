package com.nest.diamond.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nest.diamond.model.domain.TicketToken;
import com.nest.diamond.model.domain.query.TicketTokenQuery;

import java.util.List;
import java.util.Map;

public interface TicketTokenIService extends IService<TicketToken> {

    /** DataTables 查询接口 */
    List<TicketToken> search(TicketTokenQuery ticketTokenQuery);

    TicketToken findByTicketNoAndToken(String ticketNo, String token);
    
}