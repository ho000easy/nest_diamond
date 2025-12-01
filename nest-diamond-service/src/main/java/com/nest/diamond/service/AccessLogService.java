package com.nest.diamond.service;

import com.nest.diamond.iservice.AccessLogIService;
import com.nest.diamond.model.domain.AccessLog;
import com.nest.diamond.model.domain.query.AccessLogQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class AccessLogService {

    @Autowired
    private AccessLogIService accessLogIService;

    @Transactional
    public void insert(AccessLog accessLog) {
        accessLogIService.save(accessLog);
    }

    public long countToday(String module) {
        return accessLogIService.countToday(module);
    }

    public List<AccessLog> search(AccessLogQuery query) {
        return accessLogIService.search(query);
    }

}