package com.nest.diamond.dubbo.api;

import com.nest.diamond.dubbo.dto.RpcResult;
import com.nest.diamond.dubbo.dto.sync.SyncDiamondAccountsToLocalRequest;
import com.nest.diamond.dubbo.dto.sync.SyncDiamondAccountsToLocalResponse;
import com.nest.diamond.dubbo.dto.sync.SyncLocalAccountsToDiamondRequest;
import jakarta.validation.Valid;

public interface SyncAccountDubboService {
    RpcResult<Void> syncLocalAccountsToDiamond(@Valid SyncLocalAccountsToDiamondRequest syncLocalAccountsToDiamondRequest);

    RpcResult<SyncDiamondAccountsToLocalResponse> syncDiamondAccountsToLocal(@Valid SyncDiamondAccountsToLocalRequest syncDiamondAccountsToLocalRequest);
}
