package com.nest.diamond.dubbo.api;

import com.nest.diamond.dubbo.dto.sync.SyncContractInstanceRequest;

public interface SyncContractInstanceDubboService {
    void syncContractInstance(SyncContractInstanceRequest syncContractInstanceRequest);
}
