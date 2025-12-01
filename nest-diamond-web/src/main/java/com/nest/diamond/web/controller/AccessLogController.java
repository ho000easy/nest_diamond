package com.nest.diamond.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nest.diamond.model.domain.AccessLog;
import com.nest.diamond.model.domain.query.AccessLogQuery;
import com.nest.diamond.service.AccessLogService;
import com.nest.diamond.web.vo.DataTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/accessLog")
public class AccessLogController {

    @Autowired
    private AccessLogService accessLogService;

    @RequestMapping()
    public ModelAndView view() {
        return new ModelAndView("accessLog");
    }

    @RequestMapping("/search")
    @ResponseBody
    public DataTableVO<AccessLog> search(AccessLogQuery query) {
        List<AccessLog> result = accessLogService.search(query);
        return DataTableVO.create(result);
    }
}