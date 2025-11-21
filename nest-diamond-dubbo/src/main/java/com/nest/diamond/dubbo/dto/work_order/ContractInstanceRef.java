package com.nest.diamond.dubbo.dto.work_order;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ContractInstanceRef implements Serializable {
    private Long chainId;
    private String chainName;

    private String protocolName;
    private String contractName;

    private String address;
}