package com.nest.diamond.model.domain.query;

import com.nest.diamond.common.enums.TicketStatus;
import lombok.Data;

@Data
public class TicketQuery {
    private String name;
    private String ticketNo;
    private TicketStatus status;
    private String airdropOperationName;
}