// SignatureLogIService.java
package com.nest.diamond.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nest.diamond.model.domain.SignatureLog;
import com.nest.diamond.model.domain.query.SignatureLogQuery;

import java.util.List;

public interface SignatureLogIService extends IService<SignatureLog> {
    List<SignatureLog> search(SignatureLogQuery query);
}