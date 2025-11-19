
package com.nest.diamond.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nest.diamond.common.enums.VMType;
import com.nest.diamond.iservice.BlockChainIService;
import com.nest.diamond.mapper.BlockchainMapper;
import com.nest.diamond.model.domain.BlockChain;
import com.nest.diamond.model.domain.query.BlockChainQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockChainIServiceImpl extends ServiceImpl<BlockchainMapper, BlockChain> implements BlockChainIService {

    @Override
    public BlockChain findByChainName(String chainName) {
        LambdaQueryWrapper<BlockChain> queryWrapper = new QueryWrapper<BlockChain>().lambda();
        queryWrapper.eq(BlockChain::getChainName, chainName);
        return super.getOne(queryWrapper);
    }

    @Override
    public BlockChain findByChainId(Long chainId) {
        LambdaQueryWrapper<BlockChain> queryWrapper = new QueryWrapper<BlockChain>().lambda();
        queryWrapper.eq(BlockChain::getChainId, chainId);
        return super.getOne(queryWrapper);
    }

    @Override
    public List<BlockChain> allChains() {
        LambdaQueryWrapper<BlockChain> queryWrapper = new QueryWrapper<BlockChain>().lambda();
        return super.list(queryWrapper);
    }

    @Override
    public List<BlockChain> evmChains() {
        LambdaQueryWrapper<BlockChain> queryWrapper = new QueryWrapper<BlockChain>().lambda();
        queryWrapper.eq(BlockChain::getIsSupportWeb3j, true);
        return super.list(queryWrapper);
    }

    @Override
    public List<BlockChain> starkNetChains() {
        LambdaQueryWrapper<BlockChain> queryWrapper = new QueryWrapper<BlockChain>().lambda();
        queryWrapper.eq(BlockChain::getVmType, VMType.STARK_NET);
        return super.list(queryWrapper);
    }

    @Override
    public List<BlockChain> search(BlockChainQuery blockChainQuery) {
        return baseMapper.search(blockChainQuery);

    }

    @Override
    public List<BlockChain> findByVmType(VMType vmType) {
        LambdaQueryWrapper<BlockChain> queryWrapper = new QueryWrapper<BlockChain>().lambda();
        queryWrapper.eq(BlockChain::getVmType, vmType);
        return super.list(queryWrapper);
    }
}
