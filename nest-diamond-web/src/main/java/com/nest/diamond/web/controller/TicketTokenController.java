package com.nest.diamond.web.controller;

import com.nest.diamond.common.enums.TicketTokenStatusEnum;
import com.nest.diamond.model.domain.TicketToken;
import com.nest.diamond.model.domain.query.TicketTokenQuery;
import com.nest.diamond.model.vo.ApiResult;
import com.nest.diamond.service.TicketTokenService;
import com.nest.diamond.web.vo.DataTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/ticketToken")
public class TicketTokenController {

    @Autowired
    private TicketTokenService ticketTokenService;

    @GetMapping
    public ModelAndView ticketToken(String ticketNo) {
        ModelAndView modelAndView = new ModelAndView("ticketToken");

        modelAndView.addObject("statusList", TicketTokenStatusEnum.values());
        modelAndView.addObject("ticketNo", ticketNo);
        return modelAndView;
    }

    @PostMapping("/search")
    @ResponseBody
    public DataTableVO<TicketToken> search(TicketTokenQuery ticketTokenQuery) {
        return DataTableVO.create(ticketTokenService.search(ticketTokenQuery));
    }


    @PostMapping("/invalidate")
    @ResponseBody
    public ApiResult invalidate(@RequestBody List<Long> ids) {
        ticketTokenService.invalidate(ids);
        return ApiResult.success("操作成功");
    }
    
    @PostMapping("/delete")
    @ResponseBody
    public ApiResult<String> delete(@RequestBody List<Long> ids) {
        ticketTokenService.deleteByIds(ids);
        return ApiResult.success("删除成功");
    }
}