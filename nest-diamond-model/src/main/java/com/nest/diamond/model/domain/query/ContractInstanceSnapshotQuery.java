package com.nest.diamond.model.domain.query;

import lombok.Data;

@Data
public class ContractInstanceSnapshotQuery {
    private String ticketNo;
    private String protocolName;
    private String chainName;
    private String contractName;
    private String address;
}