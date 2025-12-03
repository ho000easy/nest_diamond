package com.nest.diamond.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nest.diamond.common.util.AES;
import com.nest.diamond.model.bo.AirdropItemExtend;
import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.vo.IndexValue;
import com.nest.diamond.model.vo.WalletReq;
import com.nest.diamond.service.AccountService;
import com.nest.diamond.service.AirdropService;
import com.nest.diamond.web.anno.LogAccess;
import com.nest.diamond.web.anno.UnLock;
import com.nest.diamond.web.vo.DataTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequestMapping("")
@Controller
public class WalletController {

    @Autowired
    private AirdropService airdropService;
    @Autowired
    private AccountService accountService;

    @RequestMapping("/wallet")
    public ModelAndView wallet() {
        ModelAndView modelAndView = new ModelAndView("wallet");
        modelAndView.addObject("airdropList", airdropService.allAirdrops());
        return modelAndView;
    }

    @LogAccess(value = "show_private_key", dailyLimit = 20)
    @UnLock
    @RequestMapping("/wallet/list")
    @ResponseBody
    public DataTableVO<IndexValue> list(WalletReq walletReq) {
        // 根据 isShowSeed 返回 seed 或 privateKey，不做任何截取
        List<IndexValue> indexValueList = buildIndexValue(walletReq, account ->
                walletReq.getIsShowSeed() ? account.getSeed() : account.getPrivateKey());
        Assert.isTrue(indexValueList.size() <= 5, "单次导出不能超过5个账户");

        indexValueList.forEach(item -> {
            String encryptedValue = AES.encryptWithPassword(item.getValue(), walletReq.getUnlockPassword());
            item.setValue(encryptedValue);
        });
        return DataTableVO.create(indexValueList);
    }

    @RequestMapping("/wallet/addressList")
    @ResponseBody
    public DataTableVO<IndexValue> addressList(WalletReq walletReq) {
        return DataTableVO.create(buildIndexValue(walletReq, Account::getAddress));
    }

    private List<IndexValue> buildIndexValue(WalletReq walletReq, Function<Account, String> appliedFunction) {
        List<AirdropItemExtend> airdropItemExtendList = airdropService.findAirdropItemExtendByAirdropId(walletReq.getAirdropId(), walletReq.getStartSequence(), walletReq.getEndSequence(), walletReq.getSequenceList());
        Map<String, AirdropItemExtend> airdropItemExtendMap = Maps.uniqueIndex(airdropItemExtendList, AirdropItemExtend::getAccountAddress);
        List<String> addressList = airdropItemExtendList.stream().map(AirdropItemExtend::getAccountAddress).toList();
        if(addressList.isEmpty()){
            return Lists.newArrayList();
        }
        List<Account> accountList = accountService.findByAddresses(addressList);

        List<IndexValue> indexValueList = accountList.stream().map(account -> {
            AirdropItemExtend airdropItemExtend = airdropItemExtendMap.get(account.getAddress());
            return new IndexValue(airdropItemExtend.getSequence(), appliedFunction.apply(account));
        }).collect(Collectors.toList());
        Collections.sort(indexValueList, Comparator.comparing(IndexValue::getIndex));
        Assert.isTrue(indexValueList.size() <= 5, "单次导出数量不能超过5个");
        return indexValueList;
    }



}
