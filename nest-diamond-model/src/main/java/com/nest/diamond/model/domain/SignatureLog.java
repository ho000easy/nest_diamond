// src/main/java/com/nest/diamond/model/domain/SignatureLog.java
package com.nest.diamond.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.nest.diamond.common.enums.SignType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@TableName("signature_log")
public class SignatureLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 签名地址（钱包地址） */
    private String signAddress;

    private String workOrderNo;
    /** 业务订单号（你的业务唯一标识） */
    private String bizOrderNo;

    /** 空投执行ID（关联工单） */
    private Long airdropOperationId;
    private String airdropOperationName;
    /** 链ID */
    private Long chainId;

    /** 链名称 */
    private String chainName;

    private String tx;

    /** 原始数据（JSON 或 hex） */
    private String rawData;

    /** 签名后数据（已签名的交易或消息） */
    private String signedData;

    /** 签名时间 */
    private Date signTime;
    /** 签名类型 */
    private SignType signType;

    /** 合约实例快照ID（关联快照表） */
    private Long contractInstanceSnapshotId;

    /** 合约名称 */
    private String contractName;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;
}