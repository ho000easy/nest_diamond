// src/main/java/com/nest/diamond/dubbo/dto/CreateWorkOrderResponse.java
package com.nest.diamond.dubbo.dto.work_order;

import lombok.Data;

@Data
public class CreateWorkOrderResponse {
    private String workOrderNo;      // 生成的工单编号
    private Long workOrderId;        // 工单ID

    public static CreateWorkOrderResponse create(String workOrderNo, Long id) {
        CreateWorkOrderResponse resp = new CreateWorkOrderResponse();
        resp.setWorkOrderNo(workOrderNo);
        resp.setWorkOrderId(id);
        return resp;
    }
}