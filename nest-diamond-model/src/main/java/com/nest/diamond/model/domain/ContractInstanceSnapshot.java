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

    private Long contractInstanceId;     // 关联的 contract_instance.id
    private String workOrderNo;          // 工单号（业务唯一标识）
    private Integer version;             // 版本号（从 1 开始）

    private String address;
    private Long contractId;
    private String contractName;
    private Long chainId;
    private String chainName;
    private Long protocolId;
    private String protocolName;

    private Boolean isMonitor;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;
}