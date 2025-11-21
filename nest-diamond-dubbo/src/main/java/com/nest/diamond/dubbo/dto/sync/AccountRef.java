package com.nest.diamond.dubbo.dto.sync;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountRef implements Serializable {

    private String type;
    private String address;

    private String privateKey;

    private String publicKey;

    private String seed;
    private String seedPrefix;

    private Integer hdIndex;

    private String walletVendor;
}
