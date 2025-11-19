package com.nest.diamond.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nest.diamond.common.enums.VMType;
import com.nest.diamond.model.domain.BlockChain;
import com.nest.diamond.model.domain.query.BlockChainQuery;

import java.util.List;

public interface BlockChainIService extends IService<BlockChain> {
    BlockChain findByChainName(String chainName);
    BlockChain findByChainId(Long chainId);
    List<BlockChain> allChains();

    List<BlockChain> evmChains();

    List<BlockChain> starkNetChains();

    List<BlockChain> search(BlockChainQuery blockChainQuery);

    List<BlockChain> findByVmType(VMType vmType);
}
