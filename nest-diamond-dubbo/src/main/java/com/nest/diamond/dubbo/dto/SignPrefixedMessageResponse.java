package com.nest.diamond.dubbo.dto;

import lombok.Data;

@Data
public class SignPrefixedMessageResponse extends AbstractSignResponse{
    private String signature;
}
