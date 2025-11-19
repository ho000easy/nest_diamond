package com.nest.diamond.model.domain.query;

import lombok.Data;

@Data
public class ContractInstanceQuery {
    private Long protocolId;
    private Long contractId;
    private Long chainId;
    private String contractName;
    private String address;
    private Boolean isMonitor;
}
