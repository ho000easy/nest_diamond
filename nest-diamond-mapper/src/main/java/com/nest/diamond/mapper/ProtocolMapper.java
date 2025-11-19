package com.nest.diamond.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nest.diamond.model.domain.Protocol;
import com.nest.diamond.model.domain.query.ProtocolQuery;

import java.util.List;

public interface ProtocolMapper extends BaseMapper<Protocol> {
    List<Protocol> search(ProtocolQuery protocolQuery);

}