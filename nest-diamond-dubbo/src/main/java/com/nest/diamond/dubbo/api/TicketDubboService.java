package com.nest.diamond.dubbo.api;

import com.nest.diamond.dubbo.dto.RpcResult;
import com.nest.diamond.dubbo.dto.ticket.CreateTicketRequest;
import com.nest.diamond.dubbo.dto.ticket.CreateTicketResponse;
import com.nest.diamond.dubbo.dto.ticket.TicketDTO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 工单申请 Dubbo 接口（外部系统调用）
 */
public interface TicketDubboService {

    /**
     * 创建空投工单
     */
    RpcResult<CreateTicketResponse> createTicket(@Valid CreateTicketRequest request);

    RpcResult<List<TicketDTO>> findByTicketNos(List<String> ticketNos);
    RpcResult<TicketDTO> findByTicketNo(String ticketNo);
}