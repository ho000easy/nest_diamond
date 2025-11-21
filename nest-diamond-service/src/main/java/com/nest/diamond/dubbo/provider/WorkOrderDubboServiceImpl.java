package com.nest.diamond.dubbo.provider;

import com.nest.diamond.common.util.NumUtils;
import com.nest.diamond.dubbo.api.WorkOrderDubboService;
import com.nest.diamond.dubbo.dto.work_order.ContractInstanceRef;
import com.nest.diamond.dubbo.dto.work_order.CreateWorkOrderRequest;
import com.nest.diamond.dubbo.dto.work_order.CreateWorkOrderResponse;
import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.domain.Airdrop;
import com.nest.diamond.model.domain.ContractInstanceSnapshot;
import com.nest.diamond.model.domain.WorkOrder;
import com.nest.diamond.service.AccountService;
import com.nest.diamond.service.AirdropService;
import com.nest.diamond.service.ContractInstanceSnapshotService;
import com.nest.diamond.service.WorkOrderService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

@Service
@DubboService
public class WorkOrderDubboServiceImpl implements WorkOrderDubboService {
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private ContractInstanceSnapshotService contractInstanceSnapshotService;
    @Autowired
    private AirdropService airdropService;
    @Autowired
    private AccountService accountService;

    @Transactional
    @Override
    public CreateWorkOrderResponse createWorkOrder(CreateWorkOrderRequest request) {
        Assert.isTrue(request.getStartTime().before(request.getEndTime()), "工单开始时间必须小于结束时间");

        List<Account> accountList = accountService.findByAddresses(request.getAddressList());
        Assert.isTrue(NumUtils.xEqualsY(accountList.size(), request.getAddressList().size()),
                String.format("工单中账户个数为 %d,签名系统账户个数为 %d,两边不一致", request.getAddressList().size(), accountList.size()));
        String airdropName = String.format("%s【%s】【%s】", request.getAirdropOperationName(), request.getAirdropName(), request.getWorkOrderNo());
        airdropService.createAirdrop(airdropName, request.getAddressList());
        Airdrop airdrop = airdropService.findByName(airdropName);

        buildAndSaveContractInstanceSnapshot(request);

        WorkOrder workOrder = new WorkOrder();
        BeanUtils.copyProperties(request, workOrder);
        workOrder.setAirdropId(airdrop.getId());
        workOrder.setAirdropName(airdrop.getName());
        workOrder.setApplyTime(new Date());

        workOrderService.insert(workOrder);
        return CreateWorkOrderResponse.create(request.getWorkOrderNo(), workOrder.getId());
    }

    private void buildAndSaveContractInstanceSnapshot(CreateWorkOrderRequest request) {
        List<ContractInstanceRef> contractInstanceRefList = request.getContractInstanceRefs();
        List<ContractInstanceSnapshot> contractInstanceSnapshotList = contractInstanceRefList.stream().map(contractInstanceRef -> {
            ContractInstanceSnapshot contractInstanceSnapshot = new ContractInstanceSnapshot();
            BeanUtils.copyProperties(contractInstanceRef, contractInstanceSnapshot);
            contractInstanceSnapshot.setWorkOrderNo(request.getWorkOrderNo());
            return contractInstanceSnapshot;
        }).toList();
        contractInstanceSnapshotService.batchInsert(contractInstanceSnapshotList);
        contractInstanceSnapshotService.createBlockChainIfNotExist(contractInstanceSnapshotList);
    }

}
