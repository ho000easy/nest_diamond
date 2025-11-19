package com.nest.diamond.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@TableName(autoResultMap = true)
public class Contract {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Long protocolId;

    private String protocolName;

    private String remark;

    private String abi;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;
}
