// src/main/java/com/nest/diamond/service/WorkOrderService.java
package com.nest.diamond.service;

import com.nest.diamond.common.enums.WorkOrderStatus;
import com.nest.diamond.iservice.WorkOrderIService;
import com.nest.diamond.model.domain.WorkOrder;
import com.nest.diamond.model.domain.query.WorkOrderQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkOrderService {

    @Autowired
    private WorkOrderIService workOrderIService;

    public List<WorkOrder> search(WorkOrderQuery query) {
        return workOrderIService.search(query);
    }

    public void insert(WorkOrder workOrder) {
        workOrderIService.save(workOrder);
    }

    public void approve(Long id) {
        WorkOrder wo = workOrderIService.getById(id);
        wo.setStatus(WorkOrderStatus.APPROVED);
        workOrderIService.updateById(wo);
    }

    public void reject(Long id) {
        WorkOrder wo = workOrderIService.getById(id);
        wo.setStatus(WorkOrderStatus.REJECTED);
        workOrderIService.updateById(wo);
    }
}