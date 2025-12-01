package com.nest.diamond.model.vo;

import com.nest.diamond.common.annos.LogMask;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IndexValue {
    private Integer index;

    @LogMask
    private String value;
}