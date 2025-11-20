package com.nest.diamond.dubbo.dto.sync;

import lombok.Data;

@Data
public class SyncContractInstanceRequest {

    private String address;

    private ContractRef contract;

    private Long chainId;
    private String chainName;
}
