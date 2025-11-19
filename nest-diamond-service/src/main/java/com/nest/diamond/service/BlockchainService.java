package com.nest.diamond.service;

import com.nest.diamond.common.enums.VMType;
import com.nest.diamond.common.util.BeanUtil;
import com.nest.diamond.iservice.BlockChainIService;
import com.nest.diamond.model.domain.BlockChain;
import com.nest.diamond.model.domain.query.BlockChainQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BlockchainService {

    @Autowired
    private BlockChainIService blockChainIService;

    public List<BlockChain> batchExport(List<Long> ids) {
        return blockChainIService.listByIds(ids);
    }

    public void batchImport(List<BlockChain> blockChains){
        blockChains.forEach(BeanUtil::cleanDOCommonField);
        blockChainIService.saveBatch(blockChains, 5000);
    }

    public List<BlockChain> allChains(){
        return blockChainIService.allChains();
    }

    public void insert(BlockChain blockchain){
        blockChainIService.save(blockchain);
    }

    public BlockChain findById(Long id){
        return blockChainIService.getById(id);
    }

    public BlockChain findByChainId(Long chainId){
        return blockChainIService.findByChainId(chainId);
    }


    public BlockChain findByChainName(String chainName){
        return blockChainIService.findByChainName(chainName);
    }

    public List<BlockChain> findByVmType(VMType vmType){
        return blockChainIService.findByVmType(vmType);
    }

    public List<BlockChain> search(BlockChainQuery blockChainQuery) {
        return blockChainIService.search(blockChainQuery);
    }

    public void updateById(BlockChain blockChain){
        blockChainIService.updateById(blockChain);
    }

    public void deleteById(Long id){
        blockChainIService.removeById(id);
    }

    public void deleteByIds(List<Long> ids){
        blockChainIService.removeBatchByIds(ids);
    }


}
