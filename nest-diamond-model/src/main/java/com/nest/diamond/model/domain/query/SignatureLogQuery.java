// src/main/java/com/nest/diamond/model/domain/query/SignatureLogQuery.java
package com.nest.diamond.model.domain.query;

import com.nest.diamond.common.enums.SignType;
import lombok.Data;

import java.util.Date;

@Data
public class SignatureLogQuery {
    private String address;
    private String bizOrderNo;
    private Long airdropOperationId;
    private Long contractInstanceSnapshotId;
    private SignType signType;
    private Date signTimeBegin;
    private Date signTimeEnd;
}