package com.nest.diamond.dubbo.dto.sign;

import com.nest.diamond.dubbo.dto.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class AbstractSignRequest extends BaseDTO {
    @NotBlank(message = "账户地址不能为空")
    private String signAddress;

    @NotNull(message = "业务订单号不能为空")
    private String bizOrderNo; //业务订单号
    @NotNull(message = "空投项目ID不能为空")
    private Long airdropOperationId; //空投操作ID
    private String remark;
}
