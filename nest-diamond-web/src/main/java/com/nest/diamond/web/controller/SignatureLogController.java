// src/main/java/com/nest/diamond/web/controller/SignatureLogController.java
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
    public String signatureLog(org.springframework.ui.Model model) {
        // 渲染链ID下拉框（假设你有区块链服务）
        model.addAttribute("chainList", blockchainService.allChains());
        // 渲染签名类型下拉框
        model.addAttribute("signTypeList", Arrays.asList(SignType.values()));
        return "signatureLog"; // thymeleaf 页面
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