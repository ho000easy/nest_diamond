package com.nest.diamond.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nest.diamond.model.domain.BlockChain;
import com.nest.diamond.model.domain.query.BlockChainQuery;

import java.util.List;

public interface BlockchainMapper extends BaseMapper<BlockChain> {
    List<BlockChain> search(BlockChainQuery blockChainQuery);
}