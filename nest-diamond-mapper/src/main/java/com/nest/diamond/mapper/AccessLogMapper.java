package com.nest.diamond.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nest.diamond.model.domain.AccessLog;

// 如果需要复杂查询，可以在这里添加方法并在 XML 中实现
// 目前基础的 BaseMapper 够用了
public interface AccessLogMapper extends BaseMapper<AccessLog> {
}