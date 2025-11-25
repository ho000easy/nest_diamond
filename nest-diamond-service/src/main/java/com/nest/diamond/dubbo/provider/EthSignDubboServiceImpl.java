package com.nest.diamond.dubbo.provider;

import com.nest.diamond.common.enums.SignType;
import com.nest.diamond.common.util.JsonUtils;
import com.nest.diamond.common.util.SignUtil;
import com.nest.diamond.dubbo.api.EthSignDubboService;
import com.nest.diamond.dubbo.dto.RpcResult;
import com.nest.diamond.dubbo.dto.sign.*;
import com.nest.diamond.model.domain.*;
import com.nest.diamond.service.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.StructuredDataEncoder;
import org.web3j.service.TxSignServiceImpl;
import org.web3j.utils.Numeric;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@DubboService
@Service
public class EthSignDubboServiceImpl implements EthSignDubboService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private SignatureLogService signatureLogService;
    @Autowired
    private BlockchainService blockchainService;
    @Autowired
    private ContractInstanceSnapshotService contractInstanceSnapshotService;

    @Transactional
    @Override
    public RpcResult<SignRawTransactionResponse> signRawTransaction(SignRawTransactionRequest signRawTransactionRequest) {
        RawTransaction rawTransaction = signRawTransactionRequest.getRawTransaction();
        Ticket ticket = ticketService.validateTicketAndAccount(signRawTransactionRequest.getAirdropOperationId(), signRawTransactionRequest.getSignAddress());
        ticketService.validateRawTransaction(ticket, rawTransaction, signRawTransactionRequest.getChainId());

        SignatureLog signatureLog = buildAndSaveSignatureLogFromRawTransaction(signRawTransactionRequest, ticket);

        SignRawTransactionResponse signRawTransactionResponse = new SignRawTransactionResponse();
        signRawTransactionResponse.setSignedRawTransaction(signatureLog.getSignedData());
        signRawTransactionResponse.setTxHash(signatureLog.getTx());
        return RpcResult.success(signRawTransactionResponse);
    }


    @Override
    public RpcResult<SignEip712MessageResponse> signEip712Message(SignEip712MessageRequest signEip712MessageRequest) {
        Ticket ticket = ticketService.validateTicketAndAccount(signEip712MessageRequest.getAirdropOperationId(), signEip712MessageRequest.getSignAddress());
//        ticketService.validateEip712Message(signEip712MessageRequest.getMessageHex());

        SignatureLog signatureLog = buildAndSaveSignatureLogFromEip712Message(signEip712MessageRequest, ticket);

        SignEip712MessageResponse signEip712MessageResponse = new SignEip712MessageResponse();
        signEip712MessageResponse.setSignature(signatureLog.getSignedData());

        return RpcResult.success(signEip712MessageResponse);
    }

    @Override
    public RpcResult<SignPrefixedMessageResponse> signPrefixedMessage(SignPrefixedMessageRequest signPrefixedMessageRequest) {
        Ticket ticket = ticketService.validateTicketAndAccount(signPrefixedMessageRequest.getAirdropOperationId(), signPrefixedMessageRequest.getSignAddress());
        SignatureLog signatureLog = buildAndSaveSignatureLogFromPrefixedMessage(signPrefixedMessageRequest, ticket);

        SignPrefixedMessageResponse signPrefixedMessageResponse = new SignPrefixedMessageResponse();
        signPrefixedMessageResponse.setSignature(signatureLog.getSignedData());
        return RpcResult.success(signPrefixedMessageResponse);
    }

    @SneakyThrows
    private SignatureLog buildAndSaveSignatureLogFromEip712Message(SignEip712MessageRequest signEip712MessageRequest, Ticket ticket) {
        Account account = accountService.findByAddress(signEip712MessageRequest.getSignAddress());
        StructuredDataEncoder structuredDataEncoder = signEip712MessageRequest.getDomainSeparatorHex() == null
                ? new StructuredDataEncoder(signEip712MessageRequest.getJsonMessage())
                : new CustomStructuredDataEncoder(signEip712MessageRequest.getJsonMessage(), Numeric.hexStringToByteArray(signEip712MessageRequest.getDomainSeparatorHex()));

//        String signature = SignUtil.signHashedMessage(Credentials.create(account.getPrivateKey()), Numeric.hexStringToByteArray(signEip712MessageRequest.getMessageHex()));
        byte[] hashStructuredMessage = structuredDataEncoder.hashStructuredData();
        String signature = SignUtil.signHashedMessage(Credentials.create(account.getPrivateKey()), hashStructuredMessage);

        SignatureLog signatureLog = new SignatureLog();
        signatureLog.setSignAddress(account.getAddress());
        signatureLog.setBizOrderNo(signEip712MessageRequest.getBizOrderNo());
        signatureLog.setAirdropOperationId(ticket.getAirdropOperationId());
        signatureLog.setAirdropOperationName(ticket.getAirdropOperationName());
        String rawData = signEip712MessageRequest.getDomainSeparatorHex() == null ? signEip712MessageRequest.getJsonMessage()
                : signEip712MessageRequest.getJsonMessage() + "\r\n" + signEip712MessageRequest.getDomainSeparatorHex();
        signatureLog.setRawData(rawData);
        signatureLog.setSignedData(signature);
        signatureLog.setSignTime(new Date());
        signatureLog.setSignType(SignType.ETH_EIP712);
        signatureLogService.insert(signatureLog);
        return signatureLog;
    }

    private SignatureLog buildAndSaveSignatureLogFromPrefixedMessage(SignPrefixedMessageRequest signPrefixedMessageRequest, Ticket ticket) {
        Account account = accountService.findByAddress(signPrefixedMessageRequest.getSignAddress());
        String signature = SignUtil.signPrefixedMessage(Credentials.create(account.getPrivateKey()), signPrefixedMessageRequest.getMessage());

        SignatureLog signatureLog = new SignatureLog();
        signatureLog.setSignAddress(account.getAddress());
        signatureLog.setBizOrderNo(signPrefixedMessageRequest.getBizOrderNo());
        signatureLog.setAirdropOperationId(ticket.getAirdropOperationId());
        signatureLog.setAirdropOperationName(ticket.getAirdropOperationName());

        signatureLog.setRawData(signPrefixedMessageRequest.getMessage());
        signatureLog.setSignedData(signature);
        signatureLog.setSignTime(new Date());
        signatureLog.setSignType(SignType.ETH_MESSAGE);
        signatureLogService.insert(signatureLog);
        return signatureLog;
    }

    private SignatureLog buildAndSaveSignatureLogFromRawTransaction(SignRawTransactionRequest signRawTransactionRequest, Ticket ticket) {
        RawTransaction rawTransaction = signRawTransactionRequest.getRawTransaction();

        Account account = accountService.findByAddress(signRawTransactionRequest.getSignAddress());
        TxSignServiceImpl txSignService = new TxSignServiceImpl(Credentials.create(account.getPrivateKey()));
        byte[] signedMessage = txSignService.sign(signRawTransactionRequest.getRawTransaction(), signRawTransactionRequest.getChainId());
        String signedMessageHex = Numeric.toHexString(signedMessage);

        SignatureLog signatureLog = new SignatureLog();
        signatureLog.setSignAddress(signRawTransactionRequest.getSignAddress());
        signatureLog.setBizOrderNo(signRawTransactionRequest.getBizOrderNo());
        signatureLog.setAirdropOperationId(ticket.getAirdropOperationId());
        signatureLog.setAirdropOperationName(ticket.getAirdropOperationName());

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
                    .findBy(ticket.getTicketNo(), signRawTransactionRequest.getChainId(), rawTransaction.getTo());
            signatureLog.setContractInstanceSnapshotId(snapshot.getId());
            signatureLog.setContractName(snapshot.getContractName());
        }
        signatureLogService.insert(signatureLog);
        return signatureLog;
    }


}
