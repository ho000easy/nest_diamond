// src/main/java/com/nest/diamond/service/SignatureLogService.java
package com.nest.diamond.service;

import com.nest.diamond.iservice.SignatureLogIService;
import com.nest.diamond.model.domain.SignatureLog;
import com.nest.diamond.model.domain.query.SignatureLogQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SignatureLogService {

    @Autowired
    private SignatureLogIService signatureLogIService;

    /** 复杂条件查询（唯一需要自定义的） */
    public List<SignatureLog> search(SignatureLogQuery query) {
        return signatureLogIService.search(query);
    }

    public SignatureLog findById(Long id) {
        return signatureLogIService.getById(id);
    }

    public void insert(SignatureLog signatureLog) {
        signatureLogIService.save(signatureLog);
    }

    public void deleteById(Long id) {
        signatureLogIService.removeById(id);
    }

    public void deleteByIds(List<Long> ids) {
        signatureLogIService.removeBatchByIds(ids);
    }
}