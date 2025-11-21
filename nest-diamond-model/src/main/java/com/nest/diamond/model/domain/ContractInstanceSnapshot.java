// src/main/java/com/nest/diamond/model/domain/ContractInstanceSnapshot.java
package com.nest.diamond.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@TableName("contract_instance_snapshot")
public class ContractInstanceSnapshot {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String workOrderNo;          // 工单编号（业务唯一标识）
    private Long chainId;
    private String chainName;
    private String protocolName;
    private String contractName;

    private String address;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;
}