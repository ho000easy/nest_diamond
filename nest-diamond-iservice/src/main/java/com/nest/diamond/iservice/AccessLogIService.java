package com.nest.diamond.iservice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nest.diamond.model.domain.AccessLog;
import com.nest.diamond.model.domain.query.AccessLogQuery;

import java.util.List;

public interface AccessLogIService extends IService<AccessLog> {
    List<AccessLog> search(AccessLogQuery query);

    long countToday(String module);
}