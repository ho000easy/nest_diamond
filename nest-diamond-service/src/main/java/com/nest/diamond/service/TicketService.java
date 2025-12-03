package com.nest.diamond.service;

import com.nest.diamond.common.enums.TicketStatus;
import com.nest.diamond.common.util.InnerFunctionEncoder;
import com.nest.diamond.iservice.TicketIService;
import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.domain.AirdropItem;
import com.nest.diamond.model.domain.ContractInstanceSnapshot;
import com.nest.diamond.model.domain.Ticket;
import com.nest.diamond.model.domain.query.TicketQuery;
import com.nest.diamond.model.vo.UpdateTicketReq;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.RawTransaction;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.*;

@Service
public class TicketService {

    @Autowired
    private TicketIService ticketIService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ContractInstanceSnapshotService contractInstanceSnapshotService;
    @Autowired
    private AirdropItemService airdropItemService;

    public List<Ticket> search(TicketQuery query) {
        return ticketIService.search(query);
    }

    public Ticket findById(Long id){
        return ticketIService.getById(id);
    }

    public List<Ticket> findByTicketNos(List<String> ticketNoList){
        return ticketIService.findByTicketNos(ticketNoList);
    }

    public Ticket findByTicketNo(String ticketNo){
        return ticketIService.findByTicketNo(ticketNo);
    }

    public Ticket findByTicketName(String name){
        return ticketIService.findByName(name);
    }

    public List<Ticket> findByAirdropOperationId(Long airdropOperationId){
        return ticketIService.findByAirdropOperationId(airdropOperationId);
    }

    public void insert(Ticket ticket) {
        ticketIService.save(ticket);
    }

    public void update(UpdateTicketReq updateTicketReq){
        Ticket ticket = findById(updateTicketReq.getId());
        ticket.setIsRequireContractCheck(updateTicketReq.getIsRequireContractCheck());
        ticket.setIsRequireContractFunctionCheck(updateTicketReq.getIsRequireContractFunctionCheck());
        ticketIService.updateById(ticket);
    }

    public void approve(Long id) {
        Ticket wo = ticketIService.getById(id);
        wo.setStatus(TicketStatus.APPROVED);
        ticketIService.updateById(wo);
    }

    public void reject(Long id) {
        Ticket wo = ticketIService.getById(id);
        wo.setStatus(TicketStatus.REJECTED);
        ticketIService.updateById(wo);
    }

    public void deleteByIds(List<Long> ids){
        ticketIService.removeBatchByIds(ids, 5000);
    }


    public Ticket validateTicketAndAccount(Long airdropOperationId, String signerAddress) {
        // 1. 校验账户存在
        Account account = accountService.findByAddress(signerAddress);
        Assert.notNull(account, () -> "签名账户不存在: " + signerAddress);

        // 2. 查找工单
        List<Ticket> tickets = findByAirdropOperationId(airdropOperationId);
        tickets.sort(Comparator.comparing(Ticket::getId).reversed());

        Assert.isTrue(CollectionUtils.isNotEmpty(tickets),
                () -> "空投操作ID不存在对应工单: " + airdropOperationId);

        Ticket ticket = tickets.get(0);

        AirdropItem airdropItem = airdropItemService.findByAirdropIdAndAddress(ticket.getAirdropId(), signerAddress);
        Assert.notNull(airdropItem, "空投项目下没有此地址对应的条目");

        // 3. 工单状态校验
        validateTicket(ticket);
        return ticket;
    }

    public void validateTicket(Ticket ticket) {
        // 3. 工单状态校验
        if (ticket.getStatus() == TicketStatus.PENDING) {
            throw new RuntimeException("工单还在审批中");
        }
        if (ticket.getStatus() == TicketStatus.REJECTED) {
            throw new RuntimeException("工单已被拒绝");
        }
        Date currentDate = new Date();
        if(currentDate.before(ticket.getStartTime())){
            throw new RuntimeException("工单还未到开启时间");
        }
        if(currentDate.after(ticket.getEndTime())){
            throw new RuntimeException("工单已经截至时间");
        }
    }


    public void validateRawTransaction(Ticket ticket, RawTransaction rawTx, Long chainId) {
        // 部署合约校验
        if (StringUtils.isEmpty(rawTx.getTo())) {
            Assert.isTrue(ticket.getIsAllowDeployContract(),
                    "当前工单不允许部署合约");
            return;
        }

        // 转账校验（data 为空且 value > 0）
        if (StringUtils.isEmpty(rawTx.getData())) {
            Assert.isTrue(ticket.getIsAllowTransfer(),
                    "当前工单不允许转账");
            return;
        }
        if(!ticket.getIsRequireContractCheck()){
            return;
        }

        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                "approve",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(Address.DEFAULT.toString()),
                        new org.web3j.abi.datatypes.generated.Uint256(BigInteger.ZERO)),
                Collections.<TypeReference<?>>emptyList());
        String methodId = InnerFunctionEncoder.buildMethodID(function);
        if(StringUtils.startsWith(rawTx.getData(), Numeric.cleanHexPrefix(methodId))){
            List<Type> inputParams = InnerFunctionEncoder.decodeInputData(rawTx.getData().substring(8), function);
            String spender = inputParams.get(0).toString();
            checkContractInWhiteList(ticket, spender, chainId);
        }

        // 合约白名单校验（to 不为空时）
        checkContractInWhiteList(ticket, rawTx.getTo(), chainId);
    }

    private void checkContractInWhiteList(Ticket ticket, String contractAddress, Long chainId) {
        ContractInstanceSnapshot snapshot = contractInstanceSnapshotService
                .findBy(ticket.getTicketNo(), chainId, contractAddress);
        Assert.notNull(snapshot, "合约地址不在工单白名单中: " + contractAddress);
    }


    public void validateEip712Message(String messageHex) {
        byte[] data = Numeric.hexStringToByteArray(messageHex);
        Assert.isTrue(data.length == 32, "EIP712 message 必须是 32 字节 hash");
    }


}