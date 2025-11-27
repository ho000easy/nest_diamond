package com.nest.diamond.dubbo.dto.ticket;

import com.nest.diamond.dubbo.dto.BaseDTO;
import lombok.Data;

@Data
public class CreateSyncTicketResponse extends BaseDTO {
    private String ticketNo;      // 生成的工单编号
    private String ticketToken;

    public static CreateSyncTicketResponse create(String ticketNo, String ticketToken) {
        CreateSyncTicketResponse resp = new CreateSyncTicketResponse();
        resp.setTicketNo(ticketNo);
        resp.setTicketToken(ticketToken);
        return resp;
    }
}