package com.nest.diamond.web.controller;

import com.nest.diamond.model.domain.Protocol;
import com.nest.diamond.model.domain.query.ProtocolQuery;
import com.nest.diamond.model.vo.ApiResult;
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
public class ProtocolController {

    @Autowired
    private ProtocolService protocolService;

    @RequestMapping("/protocol")
    public ModelAndView addOperation() {
        ModelAndView modelAndView = new ModelAndView("protocol");
        return modelAndView;
    }


    @RequestMapping("/protocol/add")
    @ResponseBody
    public ApiResult add(@Valid Protocol protocol) {
        protocolService.insert(protocol);
        return ApiResult.success();
    }

    @RequestMapping("/protocol/update")
    @ResponseBody
    public ApiResult update(@Valid Protocol protocol) {
        protocolService.update(protocol);
        return ApiResult.success();
    }

    @RequestMapping("/protocol/search")
    @ResponseBody
    public DataTableVO<Protocol> search(ProtocolQuery protocolQuery) {
        return DataTableVO.create(protocolService.search(protocolQuery));
    }

    @RequestMapping("/protocol/delete")
    @ResponseBody
    public ApiResult delete(@RequestBody List<Long> ids) {
        protocolService.deleteByIds(ids);
        return ApiResult.success();
    }


}
