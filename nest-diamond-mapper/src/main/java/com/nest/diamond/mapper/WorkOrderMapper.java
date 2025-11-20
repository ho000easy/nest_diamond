// WorkOrderMapper.java
package com.nest.diamond.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nest.diamond.model.domain.WorkOrder;
import com.nest.diamond.model.domain.query.WorkOrderQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkOrderMapper extends BaseMapper<WorkOrder> {
    List<WorkOrder> search(WorkOrderQuery query);
}