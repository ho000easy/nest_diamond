package com.nest.diamond.model.domain.query;

import com.nest.diamond.common.enums.WorkOrderStatus;
import lombok.Data;

import java.util.Date;

// WorkOrderQuery.java
@Data
public class WorkOrderQuery {
    private String name;
    private String workOrderNo;
    private WorkOrderStatus status;
    private String airdropOperationName;
}