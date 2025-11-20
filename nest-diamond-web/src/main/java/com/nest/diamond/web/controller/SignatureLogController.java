package com.nest.diamond.web.controller;

import com.nest.diamond.common.enums.SignType;
import com.nest.diamond.model.domain.SignatureLog;
import com.nest.diamond.model.domain.query.SignatureLogQuery;
import com.nest.diamond.model.vo.ApiResult;
import com.nest.diamond.service.BlockchainService;
import com.nest.diamond.service.SignatureLogService;
import com.nest.diamond.web.vo.DataTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/signatureLog")
public class SignatureLogController {
    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private SignatureLogService signatureLogService;

    @GetMapping
    public ModelAndView signatureLog() {
        ModelAndView modelAndView = new ModelAndView("signatureLog");

        modelAndView.addObject("chainList", blockchainService.allChains());
        modelAndView.addObject("signTypeList", Arrays.asList(SignType.values()));
        return modelAndView;
    }

    @PostMapping("/search")
    @ResponseBody
    public DataTableVO<SignatureLog> search(SignatureLogQuery query) {
        List<SignatureLog> list = signatureLogService.search(query);
        return DataTableVO.create(list);
    }

    @PostMapping("/delete")
    @ResponseBody
    public ApiResult delete(@RequestBody List<Long> ids) {
        signatureLogService.deleteByIds(ids);
        return ApiResult.success("删除成功");
    }
}