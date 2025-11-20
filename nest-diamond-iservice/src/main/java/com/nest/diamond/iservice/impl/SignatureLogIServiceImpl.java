// SignatureLogIServiceImpl.java
package com.nest.diamond.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nest.diamond.iservice.SignatureLogIService;
import com.nest.diamond.mapper.SignatureLogMapper;
import com.nest.diamond.model.domain.SignatureLog;
import com.nest.diamond.model.domain.query.SignatureLogQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignatureLogIServiceImpl extends ServiceImpl<SignatureLogMapper, SignatureLog>
        implements SignatureLogIService {

    @Override
    public List<SignatureLog> search(SignatureLogQuery query) {
        return baseMapper.search(query);
    }
}