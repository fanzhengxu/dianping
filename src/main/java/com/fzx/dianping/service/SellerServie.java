package com.fzx.dianping.service;

import com.fzx.dianping.common.BusinessException;
import com.fzx.dianping.model.SellerModel;

import java.util.List;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 20:31 2020/2/14
 */
public interface SellerServie {

    SellerModel create(SellerModel sellerModel);

    SellerModel get(Integer id);

    List<SellerModel> selectAll();

    SellerModel changeStatus(Integer id, Integer disabledFlag) throws BusinessException;

    Integer countAllSeller();
}
