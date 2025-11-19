package com.nest.diamond.web.controller;

import com.google.common.collect.Lists;
import com.nest.diamond.model.bo.AirdropItemExtend;
import com.nest.diamond.model.vo.IndexValue;
import com.nest.diamond.model.vo.WalletReq;
import com.nest.diamond.service.AirdropService;
import com.nest.diamond.web.anno.UnLock;
import com.nest.diamond.web.vo.DataTableVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@RequestMapping("")
@Controller
public class WalletController {

    @Autowired
    private AirdropService airdropService;

    @RequestMapping("/wallet")
    public ModelAndView wallet() {
        ModelAndView modelAndView = new ModelAndView("wallet");
//        modelAndView.addObject("seedList", seedService.allSeeds());
        modelAndView.addObject("airdropList", airdropService.allAirdrops());
        return modelAndView;
    }

    @UnLock
    @RequestMapping("/wallet/list")
    @ResponseBody
    public DataTableVO<IndexValue> list(WalletReq walletReq) {

        List<AirdropItemExtend> airdropItemExtendList = airdropService.findAirdropItemExtendByAirdropId(walletReq.getAirdropId(), walletReq.getStartSequence(), walletReq.getEndSequence(), walletReq.getSequenceList());
        Assert.isTrue(airdropItemExtendList.size() <= 5, "单次导出不能大于5个");

        List<IndexValue> indexValueList = Lists.newArrayList();
        for (AirdropItemExtend airdropItemExtend : airdropItemExtendList) {
            String value = walletReq.getIsShowSeed() ? airdropItemExtend.getSeed() : airdropItemExtend.getPrivateKey();
            indexValueList.add(new IndexValue(airdropItemExtend.getSequence(), value));
        }
        return DataTableVO.create(indexValueList);
    }

    @UnLock
    @RequestMapping("/wallet/split1")
    @ResponseBody
    public DataTableVO<IndexValue> split1(WalletReq walletReq) {
        List<AirdropItemExtend> airdropItemExtendList = airdropService.findAirdropItemExtendByAirdropId(walletReq.getAirdropId(), walletReq.getStartSequence(), walletReq.getEndSequence(), walletReq.getSequenceList());
        Assert.isTrue(airdropItemExtendList.size() <= 5, "单次导出不能大于5个");
        List<IndexValue> indexValueList = Lists.newArrayList();
        for (AirdropItemExtend airdropItemExtend : airdropItemExtendList) {
            String value = walletReq.getIsShowSeed() ? splitSeed(airdropItemExtend.getSeed())[0] : airdropItemExtend.getPrivateKey().substring(0, 35);

            indexValueList.add(new IndexValue(airdropItemExtend.getSequence(), value));
        }
        return DataTableVO.create(indexValueList);

    }

    @UnLock
    @RequestMapping("/wallet/split2")
    @ResponseBody
    public DataTableVO<IndexValue> split2(WalletReq walletReq) {
        List<AirdropItemExtend> airdropItemExtendList = airdropService.findAirdropItemExtendByAirdropId(walletReq.getAirdropId(), walletReq.getStartSequence(), walletReq.getEndSequence(), walletReq.getSequenceList());
        Assert.isTrue(airdropItemExtendList.size() <= 5, "单次导出不能大于5个");

        List<IndexValue> indexValueList = Lists.newArrayList();
        for (AirdropItemExtend airdropItemExtend : airdropItemExtendList) {
            String value = walletReq.getIsShowSeed() ? splitSeed(airdropItemExtend.getSeed())[1] : airdropItemExtend.getPrivateKey().substring(35);
            indexValueList.add(new IndexValue(airdropItemExtend.getSequence(), value));
        }
        return DataTableVO.create(indexValueList);

    }

    @RequestMapping("/wallet/addressList")
    @ResponseBody
    public DataTableVO<IndexValue> addressList(WalletReq walletReq) {
        List<AirdropItemExtend> airdropItemExtendList = airdropService.findAirdropItemExtendByAirdropId(walletReq.getAirdropId(), walletReq.getStartSequence(), walletReq.getEndSequence(), walletReq.getSequenceList());
        List<IndexValue> indexValueList = Lists.newArrayList();
        for (AirdropItemExtend airdropItemExtend : airdropItemExtendList) {
            indexValueList.add(new IndexValue(airdropItemExtend.getSequence(), airdropItemExtend.getAccountAddress()));
        }
        return DataTableVO.create(indexValueList);
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

    @Data
    @AllArgsConstructor
    public class WalletResp {
        private List<IndexValue> addressRespList;
        private List<IndexValue> privateKeyRespList;
    }


}
