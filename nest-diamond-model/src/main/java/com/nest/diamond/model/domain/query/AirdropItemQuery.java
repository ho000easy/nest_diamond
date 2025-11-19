package com.nest.diamond.model.domain.query;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nest.diamond.common.serializer.CommaSeparatedStringToListDeserializer;
import lombok.Data;

import java.util.List;

@Data
public class AirdropItemQuery {
    private Long airdropId;

    private Integer startSequence;
    private Integer endSequence;

    private Long seedId;

    private String address;


    @JsonDeserialize(using = CommaSeparatedStringToListDeserializer.class)
    private List<Integer> sequenceList;

}
