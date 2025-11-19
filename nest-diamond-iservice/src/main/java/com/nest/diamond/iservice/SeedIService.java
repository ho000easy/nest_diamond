package com.nest.diamond.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nest.diamond.model.domain.Seed;

import java.util.List;

public interface SeedIService extends IService<Seed> {
    Seed findBySeedPrefix(String seedPrefix);

    List<Seed> selectAll();
    List<Seed> search(String prefix);

}
