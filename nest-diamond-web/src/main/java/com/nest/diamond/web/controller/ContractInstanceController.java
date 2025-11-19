package com.nest.diamond.web.controller;

import com.nest.diamond.model.domain.ContractInstance;
import com.nest.diamond.model.domain.query.ContractInstanceQuery;
import com.nest.diamond.model.vo.ApiResult;
import com.nest.diamond.service.BlockchainService;
import com.nest.diamond.service.ContractInstanceService;
import com.nest.diamond.service.ProtocolService;
import com.nest.diamond.web.vo.DataTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ContractInstanceController {

    @Autowired
    private ProtocolService protocolService;

    @Autowired
    private ContractInstanceService contractInstanceService;

    @Autowired
    private BlockchainService blockchainService;

    @RequestMapping("/contractInstance")
    public ModelAndView contractInstance(Long protocolId, Long contractId, String contractAddress) {
        ModelAndView modelAndView = new ModelAndView("contractInstance");
        modelAndView.addObject("protocols", protocolService.allProtocols());
        modelAndView.addObject("blockChainList", blockchainService.allChains());
        modelAndView.addObject("contractAddress", contractAddress);
        modelAndView.addObject("protocolId", protocolId);
        modelAndView.addObject("contractId", contractId);

        return modelAndView;
    }

    @RequestMapping("/contractInstance/search")
    @ResponseBody
    public DataTableVO<ContractInstance> search(ContractInstanceQuery contractInstanceQuery) {
        return DataTableVO.create(contractInstanceService.search(contractInstanceQuery));
    }


    @RequestMapping("/contractInstance/delete")
    @ResponseBody
    public ApiResult delete(@RequestBody List<Long> ids) {
        contractInstanceService.deleteByIds(ids);
        return ApiResult.success();
    }


}
