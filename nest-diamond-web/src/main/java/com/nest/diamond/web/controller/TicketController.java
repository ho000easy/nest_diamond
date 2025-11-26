package com.nest.diamond.web.controller;

import com.nest.diamond.common.enums.TicketStatus;
import com.nest.diamond.model.domain.Ticket;
import com.nest.diamond.model.domain.query.TicketQuery;
import com.nest.diamond.model.vo.ApiResult;
import com.nest.diamond.service.TicketService;
import com.nest.diamond.web.vo.DataTableVO;
import com.nest.diamond.model.vo.UpdateTicketReq;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public ModelAndView ticket() {
        ModelAndView modelAndView = new ModelAndView("ticket");
        modelAndView.addObject("ticketStatusList", TicketStatus.values());
        return modelAndView;
    }

    @PostMapping("/search")
    @ResponseBody
    public DataTableVO<Ticket> search(TicketQuery query) {
        return DataTableVO.create(ticketService.search(query));
    }


    @PostMapping("/approve")
    @ResponseBody
    public ApiResult approve(@RequestBody List<Long> ids) {
        ids.forEach(ticketService::approve);
        return ApiResult.success("审批通过");
    }

    @RequestMapping("/update")
    @ResponseBody
    public ApiResult update(@Valid UpdateTicketReq updateTicketReq) {
        ticketService.update(updateTicketReq);
        return ApiResult.success();
    }

    @PostMapping("/reject")
    @ResponseBody
    public ApiResult reject(@RequestBody List<Long> ids) {
        ids.forEach(ticketService::reject);
        return ApiResult.success("已拒绝");
    }

    @PostMapping("/delete")
    @ResponseBody
    public ApiResult delete(@RequestBody List<Long> ids) {
        ticketService.deleteByIds(ids);
        return ApiResult.success("已删除");
    }
}