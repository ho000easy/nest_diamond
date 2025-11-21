package com.nest.diamond.dubbo.dto.sync;

import lombok.Data;

import java.util.List;

@Data
public class SyncAccountRequest {
    private List<AccountRef> accounts;
}
