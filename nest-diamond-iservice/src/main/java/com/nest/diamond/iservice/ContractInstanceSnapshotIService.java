// IContractInstanceSnapshotIService.java
package com.nest.diamond.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nest.diamond.model.domain.ContractInstanceSnapshot;
import com.nest.diamond.model.domain.query.ContractInstanceSnapshotQuery;

import java.util.List;

public interface ContractInstanceSnapshotIService extends IService<ContractInstanceSnapshot> {
    List<ContractInstanceSnapshot> search(ContractInstanceSnapshotQuery query);
}