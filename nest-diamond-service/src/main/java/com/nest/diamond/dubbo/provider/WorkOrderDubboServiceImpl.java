package com.nest.diamond.dubbo.provider;

import com.google.common.collect.Maps;
import com.nest.diamond.common.util.ListUtil;
import com.nest.diamond.dubbo.api.WorkOrderDubboService;
import com.nest.diamond.dubbo.dto.CreateWorkOrderRequest;
import com.nest.diamond.dubbo.dto.CreateWorkOrderResponse;
import com.nest.diamond.model.domain.ContractInstance;
import com.nest.diamond.model.domain.WorkOrder;
import com.nest.diamond.service.ContractInstanceService;
import com.nest.diamond.service.ContractInstanceSnapshotService;
import com.nest.diamond.service.WorkOrderService;
import org.apache.dubbo.config.annotation.DubboService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@Service
@DubboService
public class WorkOrderDubboServiceImpl implements WorkOrderDubboService {
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private ContractInstanceService contractInstanceService;
    @Autowired
    private ContractInstanceSnapshotService contractInstanceSnapshotService;

    @Transactional
    @Override
    public CreateWorkOrderResponse createWorkOrder(CreateWorkOrderRequest request) {
        List<Long> contractInstanceIdList = buildContractInstanceIds(request);

        WorkOrder workOrder = new WorkOrder();
        BeanUtils.copyProperties(request, workOrder);
        workOrder.setContractInstanceIds(ListUtil.longsToCsv(contractInstanceIdList));

        contractInstanceSnapshotService.create(request.getWorkOrderNo(), contractInstanceIdList);
        workOrderService.insert(workOrder);
        return CreateWorkOrderResponse.create(request.getWorkOrderNo(), workOrder.getId());
    }

    @NotNull
    private List<Long> buildContractInstanceIds(CreateWorkOrderRequest request) {
        List<ContractInstance> contractInstanceList = contractInstanceService.findAll();
        Map<String, ContractInstance> contractInstanceMap = Maps.newHashMap();
        contractInstanceList.forEach(contractInstance -> {
            String key = buildKey(contractInstance.getChainId(), contractInstance.getAddress());
            contractInstanceMap.put(key, contractInstance);
        });

        return request.getContractInstanceRefs().stream().map(contractInstanceRef -> {
            String key = buildKey(contractInstanceRef.getChainId(), contractInstanceRef.getContractAddress());
            Assert.isTrue(contractInstanceMap.containsKey(key),
                    String.format("链ID %d 合约地址 %s 在diamond系统不存在", contractInstanceRef.getChainId(), contractInstanceRef.getContractAddress()));
            return contractInstanceMap.get(key).getId();
        }).toList();
    }

    private static String buildKey(Long chainId, String contractAddress) {
        String key = String.format("%d_%s", chainId, contractAddress);
        return key;
    }
}
