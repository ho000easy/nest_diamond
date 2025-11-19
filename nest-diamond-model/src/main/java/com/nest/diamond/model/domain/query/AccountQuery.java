package com.nest.diamond.model.domain.query;

import lombok.Data;

@Data
public class AccountQuery {

    private Long seedId;
    private Integer startHDIndex;
    private Integer endHDIndex;

    private String address;
    private Long exchangeId;
    private Long exchangeUserId;
    private Long exchangeAccountId;

    private String adsUserName;
    private Integer adsStartSequence;
    private Integer adsEndSequence;
    private Integer adsBrowserWalletIndex;

    private Long tokenId;

}
