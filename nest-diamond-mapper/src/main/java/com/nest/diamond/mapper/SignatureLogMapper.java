// SignatureLogMapper.java
package com.nest.diamond.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nest.diamond.model.domain.SignatureLog;
import com.nest.diamond.model.domain.query.SignatureLogQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SignatureLogMapper extends BaseMapper<SignatureLog> {
    List<SignatureLog> search(SignatureLogQuery query);
}