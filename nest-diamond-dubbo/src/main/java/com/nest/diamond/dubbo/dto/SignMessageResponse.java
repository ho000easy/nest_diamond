package com.nest.diamond.dubbo.dto;

import lombok.Data;

@Data
public class SignMessageResponse extends AbstractSignResponse{
    private String signature;
}
