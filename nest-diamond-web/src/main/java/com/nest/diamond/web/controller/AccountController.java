package com.nest.diamond.web.controller;

import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.domain.Seed;
import com.nest.diamond.model.domain.query.AccountQuery;
import com.nest.diamond.service.AccountService;
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
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private SeedService seedService;

    @Autowired
    private AirdropService airdropService;


    @RequestMapping("/account")
    public ModelAndView account(Long seedId) {

        List<Seed> seedList = seedService.allSeeds();
        ModelAndView modelAndView = new ModelAndView("account");
        modelAndView.addObject("airdropList", airdropService.allAirdrops());
        modelAndView.addObject("seedList", seedList);
        modelAndView.addObject("seedId", seedId);

        return modelAndView;
    }

    @RequestMapping("/account/search")
    @ResponseBody
    public DataTableVO<Account> search(AccountQuery accountQuery) {
        List<Account> accountList = accountService.search(accountQuery);
        return DataTableVO.create(accountList);
    }


}
