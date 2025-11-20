package com.nest.diamond.dubbo.provider;

import com.nest.diamond.common.enums.SignType;
import com.nest.diamond.common.util.JsonUtils;
import com.nest.diamond.common.util.SignUtil;
import com.nest.diamond.dubbo.api.EthSignDubboService;
import com.nest.diamond.dubbo.dto.*;
import com.nest.diamond.model.domain.*;
import com.nest.diamond.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.service.TxSignServiceImpl;
import org.web3j.utils.Numeric;

import java.util.Date;

@DubboService
@Service
public class EthSignDubboServiceImpl implements EthSignDubboService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private SignatureLogService signatureLogService;
    @Autowired
    private BlockchainService blockchainService;
    @Autowired
    private ContractInstanceSnapshotService contractInstanceSnapshotService;

    @Transactional
    @Override
    public SignRawTransactionResponse signRawTransaction(SignRawTransactionRequest signRawTransactionRequest) {
        RawTransaction rawTransaction = signRawTransactionRequest.getRawTransaction();
        WorkOrder workOrder = workOrderService.validateWorkOrderAndAccount(signRawTransactionRequest.getAirdropOperationId(), signRawTransactionRequest.getSignAddress());
        workOrderService.validateRawTransaction(workOrder, rawTransaction, signRawTransactionRequest.getChainId());

        SignatureLog signatureLog = buildAndSaveSignatureLogFromRawTransaction(signRawTransactionRequest, workOrder);

        SignRawTransactionResponse signRawTransactionResponse = new SignRawTransactionResponse();
        signRawTransactionResponse.setSignedRawTransaction(signatureLog.getSignedData());
        signRawTransactionResponse.setTxHash(signatureLog.getTx());
        return signRawTransactionResponse;
    }


    @Override
    public SignEip712MessageResponse signEip712Message(SignEip712MessageRequest signEip712MessageRequest) {
        WorkOrder workOrder = workOrderService.validateWorkOrderAndAccount(signEip712MessageRequest.getAirdropOperationId(), signEip712MessageRequest.getSignAddress());
        workOrderService.validateEip712Message(signEip712MessageRequest.getMessageHex());

        SignatureLog signatureLog = buildAndSaveSignatureLogFromEip712Message(signEip712MessageRequest, workOrder);

        SignEip712MessageResponse signEip712MessageResponse = new SignEip712MessageResponse();
        signEip712MessageResponse.setSignature(signatureLog.getSignedData());

        return signEip712MessageResponse;
    }

    @Override
    public SignPrefixedMessageResponse signPrefixedMessage(SignPrefixedMessageRequest signPrefixedMessageRequest) {
        WorkOrder workOrder = workOrderService.validateWorkOrderAndAccount(signPrefixedMessageRequest.getAirdropOperationId(), signPrefixedMessageRequest.getSignAddress());
        SignatureLog signatureLog = buildAndSaveSignatureLogFromPrefixedMessage(signPrefixedMessageRequest, workOrder);

        SignPrefixedMessageResponse signPrefixedMessageResponse = new SignPrefixedMessageResponse();
        signPrefixedMessageResponse.setSignature(signatureLog.getSignedData());
        return signPrefixedMessageResponse;
    }

    private SignatureLog buildAndSaveSignatureLogFromEip712Message(SignEip712MessageRequest signEip712MessageRequest, WorkOrder workOrder) {
        Account account = accountService.findByAddress(signEip712MessageRequest.getSignAddress());
        String signature = SignUtil.signHashedMessage(Credentials.create(account.getPrivateKey()), Numeric.hexStringToByteArray(signEip712MessageRequest.getMessageHex()));

        SignatureLog signatureLog = new SignatureLog();
        signatureLog.setSignAddress(account.getAddress());
        signatureLog.setBizOrderNo(signEip712MessageRequest.getBizOrderNo());
        signatureLog.setAirdropOperationId(workOrder.getAirdropOperationId());
        signatureLog.setAirdropOperationName(workOrder.getAirdropOperationName());

        signatureLog.setRawData(signEip712MessageRequest.getMessageHex());
        signatureLog.setSignedData(signature);
        signatureLog.setSignTime(new Date());
        signatureLog.setSignType(SignType.ETH_EIP712);
        signatureLogService.insert(signatureLog);
        return signatureLog;
    }

    private SignatureLog buildAndSaveSignatureLogFromPrefixedMessage(SignPrefixedMessageRequest signPrefixedMessageRequest, WorkOrder workOrder) {
        Account account = accountService.findByAddress(signPrefixedMessageRequest.getSignAddress());
        String signature = SignUtil.signPrefixedMessage(Credentials.create(account.getPrivateKey()), signPrefixedMessageRequest.getMessage());

        SignatureLog signatureLog = new SignatureLog();
        signatureLog.setSignAddress(account.getAddress());
        signatureLog.setBizOrderNo(signPrefixedMessageRequest.getBizOrderNo());
        signatureLog.setAirdropOperationId(workOrder.getAirdropOperationId());
        signatureLog.setAirdropOperationName(workOrder.getAirdropOperationName());

        signatureLog.setRawData(signPrefixedMessageRequest.getMessage());
        signatureLog.setSignedData(signature);
        signatureLog.setSignTime(new Date());
        signatureLog.setSignType(SignType.ETH_MESSAGE);
        signatureLogService.insert(signatureLog);
        return signatureLog;
    }

    private SignatureLog buildAndSaveSignatureLogFromRawTransaction(SignRawTransactionRequest signRawTransactionRequest, WorkOrder workOrder) {
        RawTransaction rawTransaction = signRawTransactionRequest.getRawTransaction();

        Account account = accountService.findByAddress(signRawTransactionRequest.getSignAddress());
        TxSignServiceImpl txSignService = new TxSignServiceImpl(Credentials.create(account.getPrivateKey()));
        byte[] signedMessage = txSignService.sign(signRawTransactionRequest.getRawTransaction(), signRawTransactionRequest.getChainId());
        String signedMessageHex = Numeric.toHexString(signedMessage);

        SignatureLog signatureLog = new SignatureLog();
        signatureLog.setSignAddress(signRawTransactionRequest.getSignAddress());
        signatureLog.setBizOrderNo(signRawTransactionRequest.getBizOrderNo());
        signatureLog.setAirdropOperationId(workOrder.getAirdropOperationId());
        signatureLog.setAirdropOperationName(workOrder.getAirdropOperationName());

        BlockChain blockChain = blockchainService.findByChainId(signRawTransactionRequest.getChainId());
        signatureLog.setChainId(blockChain.getChainId());
        signatureLog.setChainName(blockChain.getChainName());
        String tx = Hash.sha3(signedMessageHex);

        signatureLog.setTx(tx);
        signatureLog.setRawData(JsonUtils.toJson(signRawTransactionRequest.getRawTransaction()));
        signatureLog.setSignedData(signedMessageHex);
        signatureLog.setSignTime(new Date());
        signatureLog.setSignType(SignType.ETH_TRANSACTION);

        if(StringUtils.isNotEmpty(rawTransaction.getTo()) && StringUtils.isNotEmpty(rawTransaction.getData())){
            ContractInstanceSnapshot snapshot = contractInstanceSnapshotService
                    .findByChainIdAndContractAddress(workOrder.getWorkOrderNo(), signRawTransactionRequest.getChainId(), rawTransaction.getTo());
            signatureLog.setContractInstanceSnapshotId(snapshot.getId());
            signatureLog.setContractName(snapshot.getContractName());
        }
        signatureLogService.insert(signatureLog);
        return signatureLog;
    }

}
