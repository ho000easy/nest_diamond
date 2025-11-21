package com.nest.diamond.iservice;


import com.baomidou.mybatisplus.extension.service.IService;
import com.nest.diamond.model.domain.Contract;
import com.nest.diamond.model.domain.query.ContractQuery;

import java.util.List;

public interface ContractIService extends IService<Contract> {
    List<Contract> findBy(Long protocolId);
    List<Contract> findByProtocolName(String protocolName);
    List<Contract> search(ContractQuery contractQuery);
}
