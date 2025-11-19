package com.nest.diamond.web.controller;

import com.nest.diamond.common.enums.RpcVendor;
import com.nest.diamond.common.enums.VMType;
import com.nest.diamond.model.domain.BlockChain;
import com.nest.diamond.model.domain.query.BlockChainQuery;
import com.nest.diamond.model.vo.ApiResult;
import com.nest.diamond.service.BlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class BlockChainController {

    @Autowired
    private BlockchainService blockchainService;


    @RequestMapping("/blockChain")
    public ModelAndView blockchain() {
        ModelAndView modelAndView = new ModelAndView("blockChain");
        modelAndView.addObject("rpcVendorList", RpcVendor.names());
        modelAndView.addObject("vmTypeList", VMType.names());
        return modelAndView;
    }

    @RequestMapping("/blockChain/all")
    @ResponseBody
    public ApiResult allBlockChains() {
        return ApiResult.success(blockchainService.allChains());
    }

    @RequestMapping("/blockChain/search")
    @ResponseBody
    public ApiResult search(BlockChainQuery blockChainQuery) {

        return ApiResult.success(blockchainService.search(blockChainQuery));
    }
    @RequestMapping("/blockChain/export")
    @ResponseBody
    public ApiResult _export(@RequestBody List<Long> ids) {
        List<BlockChain> operationVOList = blockchainService.batchExport(ids);
        return ApiResult.success(operationVOList);
    }

    @RequestMapping("/blockChain/import")
    @ResponseBody
    public ApiResult _import(@RequestBody List<BlockChain> blockChains) {
        blockchainService.batchImport(blockChains);
        return ApiResult.success();
    }

    @RequestMapping("/blockChain/add")
    @ResponseBody
    public ApiResult add(BlockChain blockChain) {
        Assert.notNull(blockChain.getIsTestnet(), "是否测试网不能为空");
        blockchainService.insert(blockChain);
        return ApiResult.success();
    }

    @RequestMapping("/blockChain/update")
    @ResponseBody
    public ApiResult update(BlockChain blockChain) {
        Assert.notNull(blockChain.getIsTestnet(), "是否测试网不能为空");
        blockchainService.updateById(blockChain);
        return ApiResult.success();
    }

    @RequestMapping("/blockChain/delete")
    @ResponseBody
    public ApiResult delete(@RequestBody List<Long> ids) {
        blockchainService.deleteByIds(ids);
        return ApiResult.success();
    }
}
