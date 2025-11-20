package com.nest.diamond.model.domain.query;

import com.nest.diamond.common.enums.WorkOrderStatus;
import lombok.Data;

import java.util.Date;

// WorkOrderQuery.java
@Data
public class WorkOrderQuery {
    private String name;
    private String workOrderNo;
    private Long airdropId;               // ← 改名
    private WorkOrderStatus status;       // ← 用枚举
    private Integer startSequence;        // ← Integer
    private Integer endSequence;          // ← Integer
    private Date createTimeBegin;
    private Date createTimeEnd;
}