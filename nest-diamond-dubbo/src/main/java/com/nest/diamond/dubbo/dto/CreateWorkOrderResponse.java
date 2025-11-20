// src/main/java/com/nest/diamond/dubbo/dto/CreateWorkOrderResponse.java
package com.nest.diamond.dubbo.dto;

import lombok.Data;

@Data
public class CreateWorkOrderResponse {
    private String workOrderNo;      // 生成的工单号
    private Long workOrderId;        // 工单ID
    private String message = "工单创建成功";

    public static CreateWorkOrderResponse success(String workOrderNo, Long id) {
        CreateWorkOrderResponse resp = new CreateWorkOrderResponse();
        resp.setWorkOrderNo(workOrderNo);
        resp.setWorkOrderId(id);
        return resp;
    }

    public static CreateWorkOrderResponse fail(String msg) {
        CreateWorkOrderResponse resp = new CreateWorkOrderResponse();
        resp.setMessage(msg);
        return resp;
    }
}