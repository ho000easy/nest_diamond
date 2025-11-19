package com.nest.diamond.web.controller;

import com.google.common.collect.Lists;
import com.nest.diamond.common.util.RandomSequenceGenerator;
import com.nest.diamond.model.domain.Airdrop;
import com.nest.diamond.model.domain.AirdropItem;
import com.nest.diamond.model.vo.AddAirdropReq;
import com.nest.diamond.model.vo.ApiResult;
import com.nest.diamond.service.AccountService;
import com.nest.diamond.service.AirdropItemService;
import com.nest.diamond.service.AirdropService;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AirdropController {

    @Autowired
    private AirdropService airdropService;
    @Autowired
    private AirdropItemService airdropItemService;

    @Autowired
    private SeedService seedService;

    @Autowired
    private AccountService accountService;


    @RequestMapping("/airdrop")
    public ModelAndView airdrop() {
        ModelAndView modelAndView = new ModelAndView("airdrop");
        modelAndView.addObject("seedList", seedService.allSeeds());

        return modelAndView;
    }

    @RequestMapping("/airdrop/search")
    @ResponseBody
    public DataTableVO<Airdrop> search(String name) {
        return DataTableVO.create(airdropService.search(name));
    }


    @RequestMapping("/airdrop/add")
    @ResponseBody
    public ApiResult add(@Valid @RequestBody AddAirdropReq addAirdropReq) {
        Airdrop airdrop = airdropService.findByName(addAirdropReq.getAirdropName());
        if (airdrop != null) {
            return ApiResult.fail("项目已存在");
        }

        List<String> addressList = buildAddresses(addAirdropReq);
        airdropService.createAirdrop(addAirdropReq.getAirdropName(), addressList, addAirdropReq.getRemark());
        return ApiResult.success();
    }

    @RequestMapping("/airdrop/update")
    @ResponseBody
    public ApiResult update(@Valid @RequestBody Airdrop airdrop) {
        airdropService.update(airdrop);
        return ApiResult.success();
    }


    @RequestMapping("/airdrop/append")
    @ResponseBody
    public ApiResult append(@Valid @RequestBody AddAirdropReq addAirdropReq) {
        Airdrop airdrop = airdropService.findByName(addAirdropReq.getAirdropName());
        Assert.notNull(airdrop, String.format("空投项目【%s】不存在", addAirdropReq.getAirdropName()));

        List<String> addressList = buildAddresses(addAirdropReq);
        airdropItemService.batchCreateAirdropItem(airdrop.getName(), addressList);
        return ApiResult.success();
    }

    @RequestMapping("/airdrop/all")
    @ResponseBody
    public ApiResult doAddAirdrop() {
        List<Airdrop> airdropList = airdropService.allAirdrops();
        return ApiResult.success(airdropList);
    }


    private List<String> buildAddresses(AddAirdropReq addAirdropReq) {
        List<String> addressList = Lists.newArrayList();
        List<AddAirdropReq.SeedConfig> includeSeedConfigList = addAirdropReq.getSeedConfigs().stream()
                .filter(seedConfig -> !seedConfig.getIsExcluded()).collect(Collectors.toList());
        List<AddAirdropReq.SeedConfig> excludeSeedConfigList = addAirdropReq.getSeedConfigs().stream()
                .filter(AddAirdropReq.SeedConfig::getIsExcluded).collect(Collectors.toList());

        includeSeedConfigList.forEach(seedConfig -> {
            List<String> _airdropAddressList = getAddresses(seedConfig);
            List<String> airdropAddressList = _airdropAddressList.stream()
                    .filter(_airdropAddress -> !addressList.contains(_airdropAddress))
                    .collect(Collectors.toList());
            addressList.addAll(airdropAddressList);
        });

        excludeSeedConfigList.forEach(seedConfig -> {
            List<String> _airdropAddressList = getAddresses(seedConfig);
            addressList.removeAll(_airdropAddressList);
        });

        if (addAirdropReq.getIsDisorder() != null && addAirdropReq.getIsDisorder()) {
            Collections.shuffle(addressList);
        }
        return addressList.stream().distinct().collect(Collectors.toList());
    }

    private List<String> getAddresses(AddAirdropReq.SeedConfig seedConfig) {
        if (seedConfig.getIsProject()) {
            List<AirdropItem> airdropItemList;
            if (seedConfig.getIsOrder()) {
                airdropItemList = airdropItemService.findByAirdropIdAndSequence(seedConfig.getProjectId(), seedConfig.getStartIndex(), seedConfig.getEndIndex());
            } else {
                List<Integer> sequenceList = seedConfig.getSequenceList();
                List<Integer> randomSequenceList = sequenceList != null && !sequenceList.isEmpty() ? sequenceList :
                        RandomSequenceGenerator.getRandomSequence(seedConfig.getStartIndex(), seedConfig.getEndIndex(), seedConfig.getDisOrderSequenceSize());
                airdropItemList = airdropItemService.findByAirdropIdAndSequence(seedConfig.getProjectId(), randomSequenceList);
            }
            return airdropItemList.stream().map(AirdropItem::getAccountAddress).collect(Collectors.toList());
        }

        if (seedConfig.getIsOrder()) {
            return accountService.findAddresses(seedConfig.getSeedId(), seedConfig.getStartIndex(), seedConfig.getEndIndex());
        }
        List<Integer> randomSequenceList = RandomSequenceGenerator.getRandomSequence(seedConfig.getStartIndex(), seedConfig.getEndIndex(), seedConfig.getDisOrderSequenceSize());
        return accountService.findAddresses(seedConfig.getSeedId(), randomSequenceList);
    }

    @RequestMapping("/airdrop/delete")
    @ResponseBody
    public ApiResult delete(@RequestBody List<Long> ids) {
        airdropService.deleteByIds(ids);
        return ApiResult.success();
    }

}
