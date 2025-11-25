package com.nest.diamond.dubbo.dto.ticket;

import com.nest.diamond.dubbo.dto.BaseDTO;
import lombok.Data;

@Data
public class CreateTicketResponse extends BaseDTO {
    private String ticketNo;      // 生成的工单编号
    private Long ticketId;        // 工单ID

    public static CreateTicketResponse create(String ticketNo, Long id) {
        CreateTicketResponse resp = new CreateTicketResponse();
        resp.setTicketNo(ticketNo);
        resp.setTicketId(id);
        return resp;
    }
}