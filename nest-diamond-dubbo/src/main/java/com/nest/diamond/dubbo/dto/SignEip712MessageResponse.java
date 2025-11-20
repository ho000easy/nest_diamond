package com.nest.diamond.dubbo.dto;

import lombok.Data;

@Data
public class SignEip712MessageResponse extends AbstractSignResponse{
    private String signature;
}
