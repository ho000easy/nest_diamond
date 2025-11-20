// IWorkOrderIService.java
package com.nest.diamond.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nest.diamond.model.domain.WorkOrder;
import com.nest.diamond.model.domain.query.WorkOrderQuery;

import java.util.List;

public interface WorkOrderIService extends IService<WorkOrder> {
    List<WorkOrder> search(WorkOrderQuery query);
    WorkOrder createWorkOrder(WorkOrder workOrder);
}