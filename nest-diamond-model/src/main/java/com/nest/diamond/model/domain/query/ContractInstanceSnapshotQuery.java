package com.nest.diamond.model.domain.query;

import lombok.Data;

@Data
public class ContractInstanceSnapshotQuery {
    private String workOrderNo;
    private Long protocolName;
    private Long chainName;
    private Long contractName;
    private String address;
}