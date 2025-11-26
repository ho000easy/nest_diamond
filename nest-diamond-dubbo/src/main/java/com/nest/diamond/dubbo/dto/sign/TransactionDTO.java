package com.nest.diamond.dubbo.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 通用以太坊交易传输对象
 * 涵盖 Legacy 和 EIP-1559 所有字段
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // --- 核心公共字段 ---

    /**
     * 链ID (防止重放攻击)
     */
    private Long chainId;

    /**
     * 交易计数器 (Nonce)
     */
    private BigInteger nonce;

    /**
     * 接收方地址 (如果是创建合约则为 null 或 "")
     */
    private String to;

    /**
     * 发送金额 (单位: Wei)
     */
    private BigInteger value;

    /**
     * 交易数据 / Payload (Hex String)
     */
    private String data;

    /**
     * Gas 限制 (Gas Limit)
     */
    private BigInteger gasLimit;

    // --- Legacy 交易字段 (Type 0 & 1) ---

    /**
     * Gas 价格 (单位: Wei)
     * 仅在 Legacy 交易中使用
     */
    private BigInteger gasPrice;

    // --- EIP-1559 交易字段 (Type 2) ---

    /**
     * 最大优先费用 (小费, Miner Tip)
     * 仅在 EIP-1559 交易中使用
     */
    private BigInteger maxPriorityFeePerGas;

    /**
     * 最大总费用 (BaseFee + PriorityFee)
     * 仅在 EIP-1559 交易中使用
     */
    private BigInteger maxFeePerGas;

    // --- 辅助方法：判断交易类型 ---

    /**
     * 是否为 EIP-1559 交易
     * 判断依据：是否存在 maxFeePerGas 或 maxPriorityFeePerGas
     */
    public boolean isEip1559() {
        return maxFeePerGas != null || maxPriorityFeePerGas != null;
    }

    // --- 核心方法：转换为 Web3j 对象 ---

}