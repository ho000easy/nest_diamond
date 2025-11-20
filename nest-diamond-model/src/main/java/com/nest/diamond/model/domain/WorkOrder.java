package com.nest.diamond.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.nest.diamond.common.enums.WorkOrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@TableName("work_order")
public class WorkOrder {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 工单名称 */
    private String name;

    /** 工单号（唯一） */
    private String workOrderNo;

    private Long airdropOperationId;
    private String airdropOperationName;

    /** 空投项目ID → 改为 airdropId */
    private Long airdropId;
    private String airdropName;

    /** 起始序列 → 改为 Integer（因为空投序列一般不会超过 21 亿） */
    private Integer startSequence;

    /** 结束序列 → 改为 Integer */
    private Integer endSequence;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间（可为空） */
    private Date endTime;

    /** 关联的合约实例ID列表 */
    private String contractInstanceIds;

    /** 关联的合约实例快照ID列表 */
    private String contractInstanceSnapshotIds;

    /** 工单状态（枚举） */
    private WorkOrderStatus status;

    /** 申请人 */
    private String applicant;

    /** 申请时间（新增） */
    private Date applyTime;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;
}