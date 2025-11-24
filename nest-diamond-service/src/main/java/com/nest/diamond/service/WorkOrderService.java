// src/main/java/com/nest/diamond/service/WorkOrderService.java
package com.nest.diamond.service;

import com.nest.diamond.common.enums.WorkOrderStatus;
import com.nest.diamond.iservice.WorkOrderIService;
import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.domain.AirdropItem;
import com.nest.diamond.model.domain.ContractInstanceSnapshot;
import com.nest.diamond.model.domain.WorkOrder;
import com.nest.diamond.model.domain.query.WorkOrderQuery;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.web3j.crypto.RawTransaction;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
public class WorkOrderService {

    @Autowired
    private WorkOrderIService workOrderIService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ContractInstanceSnapshotService contractInstanceSnapshotService;
    @Autowired
    private AirdropItemService airdropItemService;

    public List<WorkOrder> search(WorkOrderQuery query) {
        return workOrderIService.search(query);
    }

    public List<WorkOrder> findByAirdropOperationId(Long airdropOperationId){
        return workOrderIService.findByAirdropOperationId(airdropOperationId);
    }

    public void insert(WorkOrder workOrder) {
        workOrderIService.save(workOrder);
    }

    public void approve(Long id) {
        WorkOrder wo = workOrderIService.getById(id);
        wo.setStatus(WorkOrderStatus.APPROVED);
        workOrderIService.updateById(wo);
    }

    public void reject(Long id) {
        WorkOrder wo = workOrderIService.getById(id);
        wo.setStatus(WorkOrderStatus.REJECTED);
        workOrderIService.updateById(wo);
    }

    public void deleteByIds(List<Long> ids){
        workOrderIService.removeBatchByIds(ids, 5000);
    }


    public WorkOrder validateWorkOrderAndAccount(Long airdropOperationId, String signerAddress) {
        // 1. 校验账户存在
        Account account = accountService.findByAddress(signerAddress);
        Assert.notNull(account, () -> "签名账户不存在: " + signerAddress);

        // 2. 查找工单
        List<WorkOrder> workOrders = findByAirdropOperationId(airdropOperationId);
        Assert.isTrue(CollectionUtils.isNotEmpty(workOrders),
                () -> "空投操作ID不存在对应工单: " + airdropOperationId);

        WorkOrder workOrder = workOrders.get(0);

        AirdropItem airdropItem = airdropItemService.findByAirdropIdAndAddress(workOrder.getAirdropId(), signerAddress);
        Assert.notNull(airdropItem, "空投项目下没有此地址对应的条目");

        // 3. 工单状态校验
        if (workOrder.getStatus() == WorkOrderStatus.PENDING) {
            throw new RuntimeException("工单还在审批中");
        }
        if (workOrder.getStatus() == WorkOrderStatus.REJECTED) {
            throw new RuntimeException("工单已被拒绝");
        }
        Date currentDate = new Date();
        if(currentDate.before(workOrder.getStartTime())){
            throw new RuntimeException("工单还未到开启时间");
        }
        if(currentDate.after(workOrder.getEndTime())){
            throw new RuntimeException("工单已经截至时间");
        }
        return workOrder;
    }


    public void validateRawTransaction(WorkOrder workOrder, RawTransaction rawTx, Long chainId) {
        // 部署合约校验
        if (StringUtils.isEmpty(rawTx.getTo())) {
            Assert.isTrue(workOrder.getIsAllowDeployContract(),
                    "当前工单不允许部署合约");
            return;
        }

        // 转账校验（data 为空且 value > 0）
        if (StringUtils.isEmpty(rawTx.getData()) && rawTx.getValue().compareTo(BigInteger.ZERO) > 0) {
            Assert.isTrue(workOrder.getIsAllowTransfer(),
                    "当前工单不允许转账");
            return;
        }

        // 合约白名单校验（to 不为空时）
        ContractInstanceSnapshot snapshot = contractInstanceSnapshotService
                .findByChainIdAndContractAddress(workOrder.getWorkOrderNo(), chainId, rawTx.getTo());
        Assert.notNull(snapshot, "合约地址不在工单白名单中: " + rawTx.getTo());
    }


    public void validateEip712Message(String messageHex) {
        byte[] data = Numeric.hexStringToByteArray(messageHex);
        Assert.isTrue(data.length == 32, "EIP712 message 必须是 32 字节 hash");
    }


}