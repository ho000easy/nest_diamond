package com.nest.diamond.dubbo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AbstractSignRequest {
    @NotBlank(message = "账户地址不能为空")
    private String accountAddress;

    @NotNull(message = "业务订单号不能为空")
    private Long bizOrderId; //业务订单号
    @NotNull(message = "空投项目ID不能为空")
    private Long airdropOperationId; //空投操作ID
    private String remark;
}
