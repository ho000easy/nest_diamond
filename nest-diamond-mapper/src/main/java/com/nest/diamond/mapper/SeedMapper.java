package com.nest.diamond.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nest.diamond.model.domain.Seed;
import com.nest.diamond.model.domain.query.SeedQuery;

import java.util.List;

public interface SeedMapper extends BaseMapper<Seed> {
    List<Seed> search(SeedQuery seedQuery);

}