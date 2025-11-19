package com.nest.diamond.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nest.diamond.common.enums.MethodIdVendor;
import com.nest.diamond.model.domain.ContractInstanceFunction;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2023-08-16
 */
public interface ContractInstanceFunctionIService extends IService<ContractInstanceFunction> {
    List<ContractInstanceFunction> findByMethodIdVendor(MethodIdVendor methodIdVendor);

}
