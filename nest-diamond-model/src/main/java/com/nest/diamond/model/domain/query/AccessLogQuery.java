package com.nest.diamond.model.domain.query;

import lombok.Data;

@Data
public class AccessLogQuery {
    private String module;
    private String ipAddress;
}