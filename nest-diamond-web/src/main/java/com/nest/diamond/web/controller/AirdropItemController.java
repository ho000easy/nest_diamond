package com.nest.diamond.web.controller;

import com.nest.diamond.model.bo.AirdropItemExtend;
import com.nest.diamond.model.domain.query.AirdropItemQuery;
import com.nest.diamond.service.AirdropItemService;
import com.nest.diamond.service.AirdropService;
import com.nest.diamond.service.SeedService;
import com.nest.diamond.web.vo.DataTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AirdropItemController {

    @Autowired
    private AirdropItemService airdropItemService;

    @Autowired
    private SeedService seedService;

    @Autowired
    private AirdropService airdropService;


    @RequestMapping("/airdropItem")
    public ModelAndView airdropItem(Long airdropId) {
        ModelAndView modelAndView = new ModelAndView("airdropItem");
        modelAndView.addObject("seedList", seedService.allSeeds());
        modelAndView.addObject("airdropList", airdropService.allAirdrops());
        modelAndView.addObject("airdropId", airdropId);

        return modelAndView;
    }

    @RequestMapping("/airdropItem/search")
    @ResponseBody
    public DataTableVO<AirdropItemExtend> search(AirdropItemQuery airdropItemQuery) {

        List<AirdropItemExtend> airdropItemExtendList = airdropItemService.search(airdropItemQuery);
        airdropItemExtendList.forEach(airdropItemExtend -> {
            airdropItemExtend.setPrivateKey(null);
            airdropItemExtend.setSeed(null);
        });

        return DataTableVO.create(airdropItemExtendList);
    }
}
