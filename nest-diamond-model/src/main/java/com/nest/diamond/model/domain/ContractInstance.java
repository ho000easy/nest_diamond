package com.nest.diamond.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@TableName(autoResultMap = true)
public class ContractInstance {
    public static final String ZERO_ADDRESS = "0x0000000000000000000000000000000000000000";

    @TableId(type = IdType.AUTO)
    private Long id;

    private String address;

    private Long contractId;
    private String contractName;

    private Long chainId;

    private String chainName;
    private Long protocolId;
    private String protocolName;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;
}
