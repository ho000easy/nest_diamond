package com.nest.diamond.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nest.diamond.iservice.AccessLogIService;
import com.nest.diamond.mapper.AccessLogMapper;
import com.nest.diamond.model.domain.AccessLog;
import com.nest.diamond.model.domain.query.AccessLogQuery;
import org.apache.commons.lang3.StringUtils; // 假设你有这个工具类，或者用 Spring 的
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AccessLogIServiceImpl extends ServiceImpl<AccessLogMapper, AccessLog> implements AccessLogIService {

    @Override
    public List<AccessLog> search(AccessLogQuery query) {
        return list(buildWrapper(query));
    }

    @Override
    public long countToday(String module) {
        // 获取今天的 00:00:00
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfDay = calendar.getTime();

        LambdaQueryWrapper<AccessLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccessLog::getModule, module);
        wrapper.ge(AccessLog::getCreateTime, startOfDay); // 大于等于今天零点

        return count(wrapper);
    }

    private LambdaQueryWrapper<AccessLog> buildWrapper(AccessLogQuery query) {
        LambdaQueryWrapper<AccessLog> wrapper = new LambdaQueryWrapper<>();
        if (query != null) {
            if (StringUtils.isNotBlank(query.getModule())) {
                wrapper.eq(AccessLog::getModule, query.getModule());
            }
            if (StringUtils.isNotBlank(query.getIpAddress())) {
                wrapper.like(AccessLog::getIpAddress, query.getIpAddress());
            }
        }
        return wrapper;
    }
}