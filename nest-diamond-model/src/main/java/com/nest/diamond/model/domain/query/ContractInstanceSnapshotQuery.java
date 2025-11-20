package com.nest.diamond.model.domain.query;

import lombok.Data;

@Data
public class ContractInstanceSnapshotQuery {
    private Long contractInstanceId;
    private String workOrderNo;
    private Integer version;
    private Long protocolId;
    private Long contractId;
    private Long chainId;
}