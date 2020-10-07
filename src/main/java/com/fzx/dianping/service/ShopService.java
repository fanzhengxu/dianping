package com.fzx.dianping.service;

import com.fzx.dianping.common.BusinessException;
import com.fzx.dianping.model.ShopModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 19:15 2020/2/15
 */
public interface ShopService {

    ShopModel crete(ShopModel shopModel) throws BusinessException;

    ShopModel get(Integer id);

    List<ShopModel> selectAll();

    Integer countAllShop();

    List<ShopModel> recommend(BigDecimal longitude, BigDecimal latitude);

    List<ShopModel> search(BigDecimal longitude, BigDecimal latitude, String keyword, Integer orderby, Integer categoryId, String tags);

    List<Map<String, Object>> searchGroupByTags(String keyword, Integer categoryId, String tags);

    Map<String, Object> searchEs(BigDecimal longitude, BigDecimal latitude, String keyword, Integer orderby, Integer categoryId, String tags) throws IOException;

}

