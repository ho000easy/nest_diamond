package com.nest.diamond.dubbo.dto.ticket;

import com.nest.diamond.dubbo.dto.BaseDTO;
import lombok.Data;

@Data
public class CreateSignTicketResponse extends BaseDTO {
    private String ticketNo;      // 生成的工单编号
    private Long ticketId;        // 工单ID

    public static CreateSignTicketResponse create(String ticketNo, Long id) {
        CreateSignTicketResponse resp = new CreateSignTicketResponse();
        resp.setTicketNo(ticketNo);
        resp.setTicketId(id);
        return resp;
    }
}