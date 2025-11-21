// 1. 新 DTO 类（放 model/domain 包下）
package com.nest.diamond.dubbo.dto.work_order;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContractInstanceRef {
    private Long chainId;
    private String chainName;

    private String protocolName;
    private String contractName;

    private String address;



}