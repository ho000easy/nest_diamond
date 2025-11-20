package com.nest.diamond.web.controller;

import com.nest.diamond.model.domain.ContractInstanceSnapshot;
import com.nest.diamond.model.domain.query.ContractInstanceSnapshotQuery;
import com.nest.diamond.service.BlockchainService;
import com.nest.diamond.service.ContractInstanceSnapshotService;
import com.nest.diamond.service.ProtocolService;
import com.nest.diamond.web.vo.DataTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ContractInstanceSnapshotController {
    @Autowired
    private ContractInstanceSnapshotService snapshotService;
    @Autowired
    private ProtocolService protocolService;
    @Autowired
    private BlockchainService blockchainService;

    @RequestMapping("/contractInstanceSnapshot")
    public ModelAndView contractInstanceSnapshot(String workOrderNo, Long protocolId, Long contractId, String contractAddress) {
        ModelAndView modelAndView = new ModelAndView("contractInstance");
        modelAndView.addObject("protocols", protocolService.allProtocols());
        modelAndView.addObject("blockChainList", blockchainService.allChains());
        modelAndView.addObject("workOrderNo", workOrderNo);
        modelAndView.addObject("contractAddress", contractAddress);
        modelAndView.addObject("protocolId", protocolId);
        modelAndView.addObject("contractId", contractId);

        return modelAndView;
    }

    @RequestMapping("/contractInstanceSnapshot/search")
    @ResponseBody
    public DataTableVO<ContractInstanceSnapshot> search(ContractInstanceSnapshotQuery query) {
        return DataTableVO.create(snapshotService.search(query));
    }

}