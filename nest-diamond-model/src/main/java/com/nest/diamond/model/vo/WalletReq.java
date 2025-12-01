package com.nest.diamond.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nest.diamond.common.serializer.CommaSeparatedStringToListDeserializer;
import lombok.Data;

import java.util.List;

@Data
public class WalletReq {
//    private Long seedId;
    private Long airdropId;
    private Integer startSequence;
    private Integer endSequence;

    private Boolean isShowSeed;

    @JsonDeserialize(using = CommaSeparatedStringToListDeserializer.class)
    private List<Integer> sequenceList;
//    private Boolean isMix;
    private String unlockPassword;
}
