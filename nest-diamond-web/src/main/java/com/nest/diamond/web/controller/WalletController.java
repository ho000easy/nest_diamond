package com.nest.diamond.web.controller;

import com.google.common.collect.Maps;
import com.nest.diamond.model.bo.AirdropItemExtend;
import com.nest.diamond.model.domain.Account;
import com.nest.diamond.model.vo.IndexValue;
import com.nest.diamond.model.vo.WalletReq;
import com.nest.diamond.service.AccountService;
import com.nest.diamond.service.AirdropService;
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

    @UnLock
    @RequestMapping("/wallet/list")
    @ResponseBody
    public DataTableVO<IndexValue> list(WalletReq walletReq) {

        return DataTableVO.create(buildIndexValue(walletReq, account ->
                walletReq.getIsShowSeed() ? account.getSeed() : account.getPrivateKey()));
    }

    @UnLock
    @RequestMapping("/wallet/split1")
    @ResponseBody
    public DataTableVO<IndexValue> split1(WalletReq walletReq) {
        return DataTableVO.create(buildIndexValue(walletReq, account ->
                walletReq.getIsShowSeed() ? splitSeed(account.getSeed())[0] : account.getPrivateKey().substring(0, 35)));
    }

    @UnLock
    @RequestMapping("/wallet/split2")
    @ResponseBody
    public DataTableVO<IndexValue> split2(WalletReq walletReq) {
        return DataTableVO.create(buildIndexValue(walletReq, account ->
                walletReq.getIsShowSeed() ?
                        splitSeed(account.getSeed())[1]
                        : account.getPrivateKey().substring(35)));

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
        List<Account> accountList = accountService.findByAddresses(addressList);

        List<IndexValue> indexValueList = accountList.stream().map(account -> {
            AirdropItemExtend airdropItemExtend = airdropItemExtendMap.get(account.getAddress());
            return new IndexValue(airdropItemExtend.getSequence(), appliedFunction.apply(account));
        }).collect(Collectors.toList());
        Collections.sort(indexValueList, Comparator.comparing(IndexValue::getIndex));
        return indexValueList;
    }

    public static String[] splitSeed(String seed) {
        if (seed == null || seed.trim().isEmpty()) {
            Assert.isTrue(false, "seed 不能为空");
        }

        String[] words = seed.trim().split("\\s+");
        if (words.length != 12) {
            // 不是12个单词时，直接返回原seed在第一段，第二段为空
            return new String[]{seed, ""};
        }

        String part1 = String.join(" ", Arrays.copyOfRange(words, 0, 6));
        String part2 = String.join(" ", Arrays.copyOfRange(words, 6, 12));

        return new String[]{part1, part2};
    }



}
