

package com.nest.diamond.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nest.diamond.iservice.ProtocolIService;
import com.nest.diamond.mapper.ProtocolMapper;
import com.nest.diamond.model.domain.Protocol;
import com.nest.diamond.model.domain.query.ProtocolQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProtocolIServiceImpl extends ServiceImpl<ProtocolMapper, Protocol> implements ProtocolIService {

    @Override
    public List<Protocol> allProtocols() {
        LambdaQueryWrapper<Protocol> queryWrapper = new QueryWrapper<Protocol>().lambda();
        return super.list(queryWrapper);

    }

    @Override
    public Protocol findBy(String name) {
        LambdaQueryWrapper<Protocol> queryWrapper = new QueryWrapper<Protocol>().lambda();
        queryWrapper.eq(Protocol::getName, name);
        return super.getOne(queryWrapper);
    }

    @Override
    public List<Protocol> search(ProtocolQuery protocolQuery) {
        return baseMapper.search(protocolQuery);
    }
}
