package com.nest.diamond.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.nest.diamond.common.enums.ProtocolVendor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@TableName(autoResultMap = true)
public class Protocol {
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "协议名称不能为空")
    private String name;
    private String website;
    private String github;
    private String discord;
    private String twitter;
    private ProtocolVendor protocolType;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal gasPriceFactor;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal gasLimitFactor;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;
}
