package com.nest.diamond.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nest.diamond.common.serializer.CommaSeparatedStringToListDeserializer;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AddAirdropReq {
    @NotEmpty(message = "空投项目名称不能为空")
    private String airdropName;
    private String remark;

    private Boolean isDisorder;

    private List<SeedConfig> seedConfigs;

    @Data
    public static class SeedConfig{
        private Boolean isProject;
        private Long projectId;
        private Long seedId;
        private Integer startIndex;
        private Integer endIndex;

        @JsonDeserialize(using = CommaSeparatedStringToListDeserializer.class)
        private List<Integer> sequenceList;

        private Boolean isOrder;
        private Boolean isExcluded;

        private Integer disOrderSequenceSize;

    }
}
