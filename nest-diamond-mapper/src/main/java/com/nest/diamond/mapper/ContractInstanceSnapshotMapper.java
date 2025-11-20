// ContractInstanceSnapshotMapper.java
package com.nest.diamond.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nest.diamond.model.domain.ContractInstanceSnapshot;
import com.nest.diamond.model.domain.query.ContractInstanceSnapshotQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContractInstanceSnapshotMapper extends BaseMapper<ContractInstanceSnapshot> {
    List<ContractInstanceSnapshot> search(ContractInstanceSnapshotQuery query);
}