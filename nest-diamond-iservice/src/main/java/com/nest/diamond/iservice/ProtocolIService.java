package com.nest.diamond.iservice;


import com.baomidou.mybatisplus.extension.service.IService;
import com.nest.diamond.model.domain.Protocol;
import com.nest.diamond.model.domain.query.ProtocolQuery;

import java.util.List;

public interface ProtocolIService extends IService<Protocol> {
    List<Protocol> allProtocols();

    Protocol findBy(String name);

    List<Protocol> search(ProtocolQuery protocolQuery);
}
