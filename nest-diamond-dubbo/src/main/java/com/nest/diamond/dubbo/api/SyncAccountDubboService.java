package com.nest.diamond.dubbo.api;

import com.nest.diamond.dubbo.dto.RpcResult;
import com.nest.diamond.dubbo.dto.sync.SyncAccountRequest;
import jakarta.validation.Valid;

public interface SyncAccountDubboService {
    RpcResult<Void> syncAccount(@Valid SyncAccountRequest syncAccountRequest);
}
