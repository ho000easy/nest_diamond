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
import com.nest.diamond.service.SeedService;
import com.nest.diamond.web.anno.LogAccess;
import com.nest.diamond.web.anno.UnLock;
import com.nest.diamond.web.vo.DataTableVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private SeedService seedService;

    @RequestMapping("/wallet")
    public ModelAndView wallet() {
        ModelAndView modelAndView = new ModelAndView("wallet");
        modelAndView.addObject("airdropList", airdropService.allAirdrops());
        modelAndView.addObject("seedList", seedService.allSeeds());
        return modelAndView;
    }

    @LogAccess(value = "show_private_key", dailyLimit = 20)
    @UnLock
    @RequestMapping("/wallet/list")
    @ResponseBody
    public DataTableVO<IndexValue> list(WalletReq walletReq) {
        boolean startAndEndSequenceIsNotNull = walletReq.getStartSequence() != null && walletReq.getEndSequence() != null;
        boolean isHasTarget = startAndEndSequenceIsNotNull || CollectionUtils.isNotEmpty(walletReq.getSequenceList()) || StringUtils.isNotEmpty(walletReq.getAddress());
        Assert.isTrue(isHasTarget, "序列和地址不能全部为空");
        // 根据 isShowSeed 返回 seed 或 privateKey，不做任何截取
        List<IndexValue> indexValueList = buildIndexValue(walletReq, account -> {
                    if (walletReq.getIsShowSeed()) {
                        Assert.isTrue(StringUtils.isNotEmpty(account.getSeed()), "账户种子不存在");
                    } else {
                        Assert.isTrue(StringUtils.isNotEmpty(account.getPrivateKey()), "账户私钥不存在");
                    }
                    return walletReq.getIsShowSeed() ? account.getSeed() : account.getPrivateKey();
                }
        );
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
//        List<AirdropItemExtend> airdropItemExtendList = airdropService.findAirdropItemExtendByAirdropId(walletReq.getAirdropId(), walletReq.getStartSequence(), walletReq.getEndSequence(), walletReq.getSequenceList());
//        Map<String, AirdropItemExtend> airdropItemExtendMap = Maps.uniqueIndex(airdropItemExtendList, AirdropItemExtend::getAccountAddress);
//        List<String> addressList = airdropItemExtendList.stream().map(AirdropItemExtend::getAccountAddress).toList();
//        if(addressList.isEmpty()){
//            return Lists.newArrayList();
//        }
        List<Account> accountList = getAccounts(walletReq);

        List<IndexValue> indexValueList = accountList.stream().map(account -> {
//            AirdropItemExtend airdropItemExtend = airdropItemExtendMap.get(account.getAddress());
            return new IndexValue(account.getHdIndex(), appliedFunction.apply(account));
        }).collect(Collectors.toList());
        Collections.sort(indexValueList, Comparator.comparing(IndexValue::getIndex));
        Assert.isTrue(indexValueList.size() <= 5, "单次导出数量不能超过5个");
        return indexValueList;
    }

    private List<Account> getAccounts(WalletReq walletReq) {
        if (StringUtils.isNotEmpty(walletReq.getAddress())) {
            return accountService.findByAddresses(Lists.newArrayList(walletReq.getAddress()));
        }
        if (CollectionUtils.isNotEmpty(walletReq.getSequenceList())) {
            return accountService.findAccounts(walletReq.getSeedId(), walletReq.getSequenceList());
        }
        return accountService.findAccounts(walletReq.getSeedId(), walletReq.getStartSequence(), walletReq.getEndSequence());
    }


}
