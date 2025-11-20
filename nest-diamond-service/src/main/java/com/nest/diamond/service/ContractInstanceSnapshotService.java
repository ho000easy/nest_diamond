package com.nest.diamond.service;

import com.nest.diamond.common.util.BeanUtil;
import com.nest.diamond.iservice.ContractInstanceSnapshotIService;
import com.nest.diamond.model.domain.ContractInstance;
import com.nest.diamond.model.domain.ContractInstanceSnapshot;
import com.nest.diamond.model.domain.query.ContractInstanceSnapshotQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractInstanceSnapshotService {

    @Autowired
    private ContractInstanceSnapshotIService contractInstanceSnapshotIService;
    @Autowired
    private ContractInstanceService contractInstanceService;

    public List<ContractInstanceSnapshot> search(ContractInstanceSnapshotQuery query) {
        return contractInstanceSnapshotIService.search(query);
    }

    public void create(String workOrderNo, List<Long> contractInstanceIdList){
        List<ContractInstance> contractInstanceList = contractInstanceService.findByIds(contractInstanceIdList);
        List<ContractInstanceSnapshot> contractInstanceSnapshotList = contractInstanceList.stream().map(contractInstance -> {
            BeanUtil.cleanDOCommonField(contractInstance);
            ContractInstanceSnapshot contractInstanceSnapshot = new ContractInstanceSnapshot();
            BeanUtils.copyProperties(contractInstance, contractInstanceSnapshot);
            contractInstanceSnapshot.setWorkOrderNo(workOrderNo);
            return contractInstanceSnapshot;
        }).toList();
        contractInstanceSnapshotIService.saveBatch(contractInstanceSnapshotList, 5000);
    }
}