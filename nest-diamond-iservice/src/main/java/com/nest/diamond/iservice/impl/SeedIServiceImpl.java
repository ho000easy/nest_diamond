

package com.nest.diamond.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nest.diamond.iservice.SeedIService;
import com.nest.diamond.mapper.SeedMapper;
import com.nest.diamond.model.domain.Seed;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeedIServiceImpl extends ServiceImpl<SeedMapper, Seed> implements SeedIService {
    @Override
    public Seed findBySeedPrefix(String seedPrefix) {
        LambdaQueryWrapper<Seed> queryWrapper = new QueryWrapper<Seed>().lambda();
        queryWrapper.eq(Seed::getSeedPrefix, seedPrefix);
        return super.getOne(queryWrapper);
    }

    @Override
    public List<Seed> selectAll() {
        LambdaQueryWrapper<Seed> queryWrapper = new QueryWrapper<Seed>().lambda();
        return super.list(queryWrapper);
    }

    @Override
    public List<Seed> search(String prefix) {
        LambdaQueryWrapper<Seed> queryWrapper = new QueryWrapper<Seed>().lambda();
        if(prefix != null){
            queryWrapper.like(Seed::getSeedPrefix, prefix);
        }
        return super.list(queryWrapper);
    }
}
