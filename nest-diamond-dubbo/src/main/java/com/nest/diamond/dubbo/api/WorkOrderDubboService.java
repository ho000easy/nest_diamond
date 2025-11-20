// src/main/java/com/nest/diamond/dubbo/api/WorkOrderDubboService.java
package com.nest.diamond.dubbo.api;

import com.nest.diamond.dubbo.dto.work_order.CreateWorkOrderRequest;
import com.nest.diamond.dubbo.dto.work_order.CreateWorkOrderResponse;
import jakarta.validation.Valid;

/**
 * 工单申请 Dubbo 接口（外部系统调用）
 */
public interface WorkOrderDubboService {

    /**
     * 创建空投工单
     */
    CreateWorkOrderResponse createWorkOrder(@Valid CreateWorkOrderRequest request);
}