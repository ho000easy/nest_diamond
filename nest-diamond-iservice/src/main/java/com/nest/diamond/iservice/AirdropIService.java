package com.nest.diamond.iservice;


import com.baomidou.mybatisplus.extension.service.IService;
import com.nest.diamond.model.domain.Airdrop;

import java.util.List;

public interface AirdropIService extends IService<Airdrop> {
    Airdrop getByName(String name);

    List<Airdrop> allAirdrops();

    List<Airdrop> search(String name);
    List<Airdrop> findByIds(List<Long> ids);
}
