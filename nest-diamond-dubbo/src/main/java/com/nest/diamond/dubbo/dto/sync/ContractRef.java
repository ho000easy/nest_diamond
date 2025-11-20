package com.nest.diamond.dubbo.dto.sync;

import lombok.Data;

@Data
public class ContractRef {

    private String name;

    private ProtocolRef protocolRef;

    private String remark;

    private String abi;
}
