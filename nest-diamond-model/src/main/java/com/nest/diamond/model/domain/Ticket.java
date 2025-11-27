package com.nest.diamond.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.nest.diamond.common.enums.TicketStatus;
import com.nest.diamond.common.enums.TicketType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@TableName("ticket")
public class Ticket {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 工单名称 */
    private String name;

    /** 工单编号（唯一） */
    private String ticketNo;
    private TicketType type;

    private Long airdropOperationId;
    private String airdropOperationName;

    /** 空投项目ID → 改为 airdropId */
    private Long airdropId;
    private String airdropName;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间（可为空） */
    private Date endTime;

    /** 工单状态（枚举） */
    private TicketStatus status;

    /** 申请人 */
    private String applicant;

    /** 申请时间（新增） */
    private Date applyTime;

    private Boolean isAllowTransfer;
    private Boolean isAllowDeployContract;

    private Boolean isRequireContractCheck;
    private Boolean isRequireContractFunctionCheck;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;
}