package com.nest.diamond.dubbo.dto.ticket;

import lombok.Data;

import java.io.Serializable;

@Data
public class ContractInstanceRef implements Serializable {
    private Long chainId;
    private String chainName;

    private String protocolName;
    private String contractName;

    private String address;
}