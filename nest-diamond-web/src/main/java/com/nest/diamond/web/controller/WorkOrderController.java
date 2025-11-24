package com.nest.diamond.web.controller;

import com.nest.diamond.common.enums.WorkOrderStatus;
import com.nest.diamond.model.domain.WorkOrder;
import com.nest.diamond.model.domain.query.WorkOrderQuery;
import com.nest.diamond.model.vo.ApiResult;
import com.nest.diamond.service.WorkOrderService;
import com.nest.diamond.web.vo.DataTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/workOrder")
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @GetMapping
    public ModelAndView workOrder() {
        ModelAndView modelAndView = new ModelAndView("workOrder");
        modelAndView.addObject("workOrderStatusList", WorkOrderStatus.values());
        return modelAndView;
    }

    @PostMapping("/search")
    @ResponseBody
    public DataTableVO<WorkOrder> search(WorkOrderQuery query) {
        return DataTableVO.create(workOrderService.search(query));
    }


    @PostMapping("/approve")
    @ResponseBody
    public ApiResult approve(@RequestBody List<Long> ids) {
        ids.forEach(workOrderService::approve);
        return ApiResult.success("审批通过");
    }

    @PostMapping("/reject")
    @ResponseBody
    public ApiResult reject(@RequestBody List<Long> ids) {
        ids.forEach(workOrderService::reject);
        return ApiResult.success("已拒绝");
    }

    @PostMapping("/delete")
    @ResponseBody
    public ApiResult delete(@RequestBody List<Long> ids) {
        workOrderService.deleteByIds(ids);
        return ApiResult.success("已删除");
    }
}