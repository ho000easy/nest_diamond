package com.nest.diamond.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTicketReq {
    @NotNull(message = "id不能为空")
    private Long id;
    @NotNull(message = "是否校验合约不能为空")
    private Boolean isRequireContractCheck;
    @NotNull(message = "是否校验合约函数不能为空")
    private Boolean isRequireContractFunctionCheck;
}
