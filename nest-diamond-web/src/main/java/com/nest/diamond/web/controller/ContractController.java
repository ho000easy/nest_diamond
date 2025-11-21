package com.nest.diamond.web.controller;

import com.nest.diamond.model.domain.Contract;
import com.nest.diamond.model.domain.query.ContractQuery;
import com.nest.diamond.model.vo.ApiResult;
import com.nest.diamond.service.ContractService;
import com.nest.diamond.service.ProtocolService;
import com.nest.diamond.web.vo.DataTableVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ContractController {

    @Autowired
    private ProtocolService protocolService;

    @Autowired
    private ContractService contractService;

    @RequestMapping("/contract")
    public ModelAndView contract(Long protocolId) {
        ModelAndView modelAndView = new ModelAndView("contract");
        modelAndView.addObject("protocols", protocolService.allProtocols());
        modelAndView.addObject("protocolId", protocolId);
        return modelAndView;
    }

    @RequestMapping("/contract/search")
    @ResponseBody
    public DataTableVO<Contract> search(ContractQuery contractQuery) {
        return DataTableVO.create(contractService.search(contractQuery));
    }

    @RequestMapping("/contract/findByProtocolId")
    @ResponseBody
    public ApiResult<List<Contract>> findByProtocolId(Long protocolId, String protocolName) {
        List<Contract> contractList = contractService.findByProtocolId(protocolId);
        return ApiResult.success(contractList);
    }

    @RequestMapping("/contract/findByProtocolName")
    @ResponseBody
    public ApiResult<List<Contract>> findByProtocolId(String protocolName) {
        List<Contract> contractList = contractService.findByProtocolName(protocolName);
        return ApiResult.success(contractList);
    }

    @RequestMapping("/contract/add")
    @ResponseBody
    public ApiResult add(@Valid Contract contract) {
        contractService.create(contract);
        return ApiResult.success();
    }

    @RequestMapping("/contract/delete")
    @ResponseBody
    public ApiResult delete(@RequestBody List<Long> ids) {
        contractService.deleteByIds(ids);
        return ApiResult.success();
    }


}
