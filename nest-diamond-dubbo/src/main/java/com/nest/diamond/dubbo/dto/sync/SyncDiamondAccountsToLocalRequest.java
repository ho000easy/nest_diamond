package com.nest.diamond.dubbo.dto.sync;

import lombok.Data;

import java.io.Serializable;

@Data
public class SyncDiamondAccountsToLocalRequest implements Serializable {
    private String ticketNo;
    private String authToken;
}
