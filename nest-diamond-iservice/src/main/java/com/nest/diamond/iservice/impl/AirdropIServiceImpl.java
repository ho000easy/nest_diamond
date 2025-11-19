package com.nest.diamond.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nest.diamond.iservice.AirdropIService;
import com.nest.diamond.mapper.AirdropMapper;
import com.nest.diamond.model.domain.Airdrop;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirdropIServiceImpl extends ServiceImpl<AirdropMapper, Airdrop> implements AirdropIService {

    @Override
    public Airdrop getByName(String name) {
        LambdaQueryWrapper<Airdrop> queryWrapper = new QueryWrapper<Airdrop>().lambda();
        queryWrapper.eq(Airdrop::getName, name);
        queryWrapper.orderByDesc(Airdrop::getId);
        return super.getOne(queryWrapper);
    }

    @Override
    public List<Airdrop> allAirdrops() {
        LambdaQueryWrapper<Airdrop> queryWrapper = new QueryWrapper<Airdrop>().lambda();
        return super.list(queryWrapper);
    }

    @Override
    public List<Airdrop> search(String name) {
        LambdaQueryWrapper<Airdrop> queryWrapper = new QueryWrapper<Airdrop>().lambda();
        if(name != null){
            queryWrapper.like(Airdrop::getName, name);
        }
        return super.list(queryWrapper);
    }

    @Override
    public List<Airdrop> findByIds(List<Long> ids) {
        LambdaQueryWrapper<Airdrop> queryWrapper = new QueryWrapper<Airdrop>().lambda();
        queryWrapper.in(Airdrop::getId, ids);

        return super.list(queryWrapper);
    }

}
