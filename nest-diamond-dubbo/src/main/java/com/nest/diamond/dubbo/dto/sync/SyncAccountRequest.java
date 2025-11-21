package com.nest.diamond.dubbo.dto.sync;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SyncAccountRequest implements Serializable {
    private List<AccountRef> accounts;
}
