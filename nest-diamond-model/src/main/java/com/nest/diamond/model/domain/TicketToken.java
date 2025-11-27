package com.nest.diamond.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.nest.diamond.common.enums.TicketTokenStatusEnum;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ticket_token")
public class TicketToken {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关联工单ID */
    private Long ticketId;

    /** 工单编号 (冗余字段，方便查询展示，建议关联查询或冗余存储) */
    private String ticketNo;

    /** 唯一Token */
    private String token;

    /** 状态 */
    private TicketTokenStatusEnum status;

    /** 过期时间 */
    private Date expireTime;

    /** 使用时间 */
    private Date useTime;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;
}