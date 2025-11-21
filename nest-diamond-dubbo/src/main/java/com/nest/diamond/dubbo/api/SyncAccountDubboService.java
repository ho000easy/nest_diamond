package com.nest.diamond.dubbo.api;

import com.nest.diamond.dubbo.dto.sync.SyncAccountRequest;

public interface SyncAccountDubboService {
    void syncAccount(SyncAccountRequest syncAccountRequest);
}
