package com.nest.diamond.dubbo.api;

import com.nest.diamond.dubbo.dto.sync.SyncAirdropRequest;

public interface SyncAirdropDubboService {
    void syncAirdrop(SyncAirdropRequest syncAirdropRequest);
}
