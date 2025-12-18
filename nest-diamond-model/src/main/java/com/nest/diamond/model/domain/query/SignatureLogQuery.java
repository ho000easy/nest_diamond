// src/main/java/com/nest/diamond/model/domain/query/SignatureLogQuery.java
package com.nest.diamond.model.domain.query;

import com.nest.diamond.common.enums.SignType;
import lombok.Data;

import java.util.Date;

@Data
public class SignatureLogQuery {
    private String ticketNo;
    private String signAddress;
    private String bizOrderNo;
    private String airdropOperationName;
    private String contractAddress;
    private Long chainId;
    private SignType signType;
}