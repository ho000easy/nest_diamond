package com.nest.diamond.dubbo.dto.ticket;

import com.nest.diamond.dubbo.dto.BaseDTO;
import com.nest.diamond.dubbo.enums.TicketStatusDTO;
import lombok.Data;

import java.util.Date;

@Data
public class TicketDTO extends BaseDTO {

    /** 工单编号（唯一） */
    private String ticketNo;

    private Long airdropOperationId;
    private String airdropOperationName;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间（可为空） */
    private Date endTime;

    /** 工单状态（枚举） */
    private TicketStatusDTO status;

    /** 申请人 */
    private String applicant;

    /** 申请时间（新增） */
    private Date applyTime;

    private Boolean isAllowTransfer;
    private Boolean isAllowDeployContract;

    private String remark;

    private Date createTime;

    private Date modifyTime;
}