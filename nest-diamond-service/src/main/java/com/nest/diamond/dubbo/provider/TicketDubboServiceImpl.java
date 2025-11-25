package com.nest.diamond.dubbo.provider;

import com.google.common.collect.Lists;
import com.nest.diamond.common.enums.TicketStatus;
import com.nest.diamond.common.util.NumUtils;
import com.nest.diamond.dubbo.api.TicketDubboService;
import com.nest.diamond.dubbo.dto.RpcResult;
import com.nest.diamond.dubbo.dto.ticket.ContractInstanceRef;
import com.nest.diamond.dubbo.dto.ticket.CreateTicketRequest;
import com.nest.diamond.dubbo.dto.ticket.CreateTicketResponse;
import com.nest.diamond.dubbo.dto.ticket.TicketDTO;
import com.nest.diamond.dubbo.enums.TicketStatusDTO;
import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.domain.Airdrop;
import com.nest.diamond.model.domain.ContractInstanceSnapshot;
import com.nest.diamond.model.domain.Ticket;
import com.nest.diamond.service.AccountService;
import com.nest.diamond.service.AirdropService;
import com.nest.diamond.service.ContractInstanceSnapshotService;
import com.nest.diamond.service.TicketService;
import org.apache.commons.collections.CollectionUtils;
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
public class TicketDubboServiceImpl implements TicketDubboService {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private ContractInstanceSnapshotService contractInstanceSnapshotService;
    @Autowired
    private AirdropService airdropService;
    @Autowired
    private AccountService accountService;

    @Transactional
    @Override
    public RpcResult<CreateTicketResponse> createTicket(CreateTicketRequest request) {
        Assert.isTrue(request.getStartTime().before(request.getEndTime()), "工单开始时间必须小于结束时间");

        List<Account> accountList = accountService.findByAddresses(request.getAddressList());
        Assert.isTrue(NumUtils.xEqualsY(accountList.size(), request.getAddressList().size()),
                String.format("工单中账户个数为 %d,签名系统账户个数为 %d,两边不一致", request.getAddressList().size(), accountList.size()));
        String airdropName = String.format("%s【%s】【%s】", request.getAirdropOperationName(), request.getAirdropName(), request.getTicketNo());
        airdropService.createAirdrop(airdropName, request.getAddressList());
        Airdrop airdrop = airdropService.findByName(airdropName);

        buildAndSaveContractInstanceSnapshot(request);
        Ticket _ticket = ticketService.findByTicketNo(request.getTicketNo());
        Assert.isNull(_ticket, String.format("工单在diamond系统已存在，工单编号：%s", request.getTicketNo()));

        Ticket __ticket = ticketService.findByTicketName(request.getName());
        Assert.isNull(__ticket, String.format("工单在diamond系统已存在，工单名称：%s", request.getName()));


        Ticket ticket = new Ticket();
        BeanUtils.copyProperties(request, ticket);
        ticket.setTicketNo(request.getTicketNo());
        ticket.setAirdropId(airdrop.getId());
        ticket.setAirdropName(airdrop.getName());
        ticket.setApplyTime(new Date());
        ticket.setStatus(TicketStatus.PENDING);

        ticketService.insert(ticket);
        return RpcResult.success(CreateTicketResponse.create(request.getTicketNo(), ticket.getId()));
    }

    @Override
    public RpcResult<List<TicketDTO>> findByTicketNos(List<String> ticketNos) {
        List<Ticket> ticketList = ticketService.findByTicketNos(ticketNos);
        List<TicketDTO> ticketDTOList = ticketList.stream().map(TicketDubboServiceImpl::buildTicketDTO).toList();
        return RpcResult.success(ticketDTOList);
    }


    @Override
    public RpcResult<TicketDTO> findByTicketNo(String ticketNo) {
        Ticket ticket = ticketService.findByTicketNo(ticketNo);
        TicketDTO ticketDTO = buildTicketDTO(ticket);
        return RpcResult.success(ticketDTO);
    }


    private void buildAndSaveContractInstanceSnapshot(CreateTicketRequest request) {
        List<ContractInstanceRef> contractInstanceRefList = request.getContractInstanceRefs();
        if(CollectionUtils.isEmpty(contractInstanceRefList)){
            return;
        }
        List<ContractInstanceSnapshot> contractInstanceSnapshotList = contractInstanceRefList.stream().map(contractInstanceRef -> {
            ContractInstanceSnapshot contractInstanceSnapshot = new ContractInstanceSnapshot();
            BeanUtils.copyProperties(contractInstanceRef, contractInstanceSnapshot);
            contractInstanceSnapshot.setTicketNo(request.getTicketNo());
            return contractInstanceSnapshot;
        }).toList();
        contractInstanceSnapshotService.batchInsert(contractInstanceSnapshotList);
        contractInstanceSnapshotService.createBlockChainIfNotExist(contractInstanceSnapshotList);
    }


    private static TicketDTO buildTicketDTO(Ticket ticket) {
        TicketDTO ticketDTO = new TicketDTO();
        BeanUtils.copyProperties(ticket, ticketDTO);
        ticketDTO.setStatus(TicketStatusDTO.valueOf(ticket.getStatus().name()));
        return ticketDTO;
    }

}
