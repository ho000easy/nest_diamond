package com.nest.diamond.model.domain.query;

import com.nest.diamond.common.enums.TicketTokenStatusEnum;
import lombok.Data;

@Data
public class TicketTokenQuery {

    private String ticketNo;

    /** 唯一Token */
    private String token;

    /** 状态 */
    private TicketTokenStatusEnum status;

}
