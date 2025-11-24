package com.nest.diamond.service;

import com.nest.diamond.iservice.ContractInstanceSnapshotIService;
import com.nest.diamond.model.domain.BlockChain;
import com.nest.diamond.model.domain.ContractInstanceSnapshot;
import com.nest.diamond.model.domain.query.ContractInstanceSnapshotQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ContractInstanceSnapshotService {

    @Autowired
    private ContractInstanceSnapshotIService contractInstanceSnapshotIService;
    @Autowired
    private ContractInstanceService contractInstanceService;
    @Autowired
    private BlockchainService blockchainService;

    public List<ContractInstanceSnapshot> search(ContractInstanceSnapshotQuery query) {
        return contractInstanceSnapshotIService.search(query);
    }

    public ContractInstanceSnapshot findByChainIdAndContractAddress(String workOrderNo, Long chainId, String contractAddress){
        return contractInstanceSnapshotIService.findByChainIdAndContractAddress(workOrderNo, chainId, contractAddress);
    }

    public void batchInsert(List<ContractInstanceSnapshot> contractInstanceSnapshotList){
        contractInstanceSnapshotIService.saveBatch(contractInstanceSnapshotList, 5000);
    }

    public void createBlockChainIfNotExist(List<ContractInstanceSnapshot> contractInstanceSnapshotList){
        List<ContractInstanceSnapshot> uniqueList = new ArrayList<>(
                contractInstanceSnapshotList.stream()
                        .collect(Collectors.toMap(
                                ContractInstanceSnapshot::getChainName,                  // 要去重的字段（换成你自己的）
                                Function.identity(),            // 值是对象本身
                                (existing, replacement) -> existing,  // 重复时保留前面的
                                LinkedHashMap::new              // 保持插入顺序
                        ))
                        .values()
        );

        uniqueList.forEach(contractInstanceSnapshot -> {
            BlockChain blockChain = blockchainService.findByChainName(contractInstanceSnapshot.getChainName());
            if(blockChain == null){
                BlockChain _blockChain = new BlockChain();
                _blockChain.setChainId(contractInstanceSnapshot.getChainId());
                _blockChain.setChainName(contractInstanceSnapshot.getChainName());
                blockchainService.insert(_blockChain);
            }
        });
    }

    @Transactional
    public void deleteByIds(List<Long> ids){
        contractInstanceSnapshotIService.removeBatchByIds(ids, 5000);
    }
}