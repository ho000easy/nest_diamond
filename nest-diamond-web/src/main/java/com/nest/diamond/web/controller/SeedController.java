package com.nest.diamond.web.controller;

import com.nest.diamond.common.enums.WalletGenerateType;
import com.nest.diamond.common.enums.WalletVendor;
import com.nest.diamond.model.domain.Seed;
import com.nest.diamond.model.domain.query.SeedQuery;
import com.nest.diamond.model.vo.AddSeedReq;
import com.nest.diamond.model.vo.ApiResult;
import com.nest.diamond.service.SeedService;
import com.nest.diamond.web.vo.DataTableVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.regex.Pattern;

@Controller
public class SeedController {


    @Autowired
    private SeedService seedService;

    @RequestMapping("/seed")
    public ModelAndView seed() {
        List<Seed> seedList = seedService.allSeeds();
        ModelAndView modelAndView = new ModelAndView("seed");
        modelAndView.addObject("seedList", seedList);
        modelAndView.addObject("walletVendorList", WalletVendor.names());
        return modelAndView;
    }

    @RequestMapping("/seed/add")
    @ResponseBody
    public ApiResult addSeed(@RequestBody @Valid AddSeedReq addSeedReq) {
        if(addSeedReq.getWalletGenerateType() != WalletGenerateType.MULTI_PRIVATE_KEY){
            Pattern pattern = Pattern.compile("^\\S+(\\s\\S+){11}$");
            addSeedReq.getSeedWordsList().forEach(seedWords -> {
                boolean isMatch = pattern.matcher(seedWords).matches();
                Assert.isTrue(isMatch, "格式不正确，必须为12个单词并且以1个空格隔开");
            });
        }
        seedService.createAccounts(addSeedReq);
        return ApiResult.success();
    }

    @RequestMapping("/seed/search")
    @ResponseBody
    public DataTableVO<Seed> seedQuery(SeedQuery seedQuery) {
        return DataTableVO.create(seedService.search(seedQuery));
    }

    @RequestMapping("/seed/all")
    @ResponseBody
    public ApiResult allSeed() {
        return ApiResult.success(seedService.allSeeds());
    }

    @RequestMapping("/seed/generate")
    @ResponseBody
    public ApiResult generateSeed() {
        return ApiResult.success(seedService.generateSeed());
    }


    @RequestMapping("/seed/delete")
    @ResponseBody
    public ApiResult delete(@RequestBody List<Long> ids) {
        seedService.deleteByIds(ids);
        return ApiResult.success();
    }


}
