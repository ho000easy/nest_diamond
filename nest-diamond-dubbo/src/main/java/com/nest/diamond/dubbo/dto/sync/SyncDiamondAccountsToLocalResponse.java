package com.nest.diamond.dubbo.dto.sync;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@AllArgsConstructor
public class SyncDiamondAccountsToLocalResponse implements Serializable {
    private List<AccountRef> accountRefList;
}
