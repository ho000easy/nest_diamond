package com.nest.diamond.dubbo.api;

import com.nest.diamond.dubbo.dto.RpcResult;
import com.nest.diamond.dubbo.dto.ticket.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 工单申请 Dubbo 接口（外部系统调用）
 */
public interface TicketDubboService {
    RpcResult<CreateSignTicketResponse> createSignTicket(@Valid CreateSignTicketRequest request);
    RpcResult<CreateSyncTicketResponse> createSyncTicket(@Valid CreateSyncTicketRequest request);

    RpcResult<List<TicketDTO>> findByTicketNos(List<String> ticketNos);
    RpcResult<TicketDTO> findByTicketNo(String ticketNo);
}