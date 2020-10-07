package com.fzx.dianping.controller;
import java.io.IOException;
import	java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fzx.dianping.common.BusinessException;
import com.fzx.dianping.common.CommonRes;
import com.fzx.dianping.common.EmBusinessError;
import com.fzx.dianping.model.CategoryModel;
import com.fzx.dianping.model.ShopModel;
import com.fzx.dianping.service.CategoryService;
import com.fzx.dianping.service.ShopService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 14:05 2020/2/16
 */
@Controller("/shop")
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/recommend")
    @ResponseBody
    public CommonRes recommend(@RequestParam(name = "longitude")BigDecimal longitude,
                               @RequestParam(name = "latitude")BigDecimal latitude) throws BusinessException {
        if (longitude == null || latitude == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        List<ShopModel> shopModelList = shopService.recommend(longitude, latitude);
        return CommonRes.create(shopModelList);
    }

    @RequestMapping("/search")
    @ResponseBody
    public CommonRes search(@RequestParam(name = "longitude")BigDecimal longitude,
                               @RequestParam(name = "latitude")BigDecimal latitude,
                            @RequestParam(name = "keyword")String keyword,
                            @RequestParam(name = "orderby", required = false)Integer orderby,
                            @RequestParam(name = "categoryId", required = false)Integer categoryId,
                            @RequestParam(name="tags",required = false)String tags) throws BusinessException, IOException {
        if (longitude == null || latitude == null || StringUtils.isBlank(keyword)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //List<ShopModel> shopModelList = shopService.search(longitude, latitude, keyword, orderby, categoryId, tags);
        Map<String, Object> result = shopService.searchEs(longitude, latitude, keyword, orderby, categoryId, tags);
        List<ShopModel> shopModelList = (List<ShopModel>) result.get("shop");

        List<CategoryModel> categoryModelList = categoryService.selectAll();
        //List<Map<String, Object>> tagsAggregation = shopService.searchGroupByTags(keyword, categoryId, tags);
        List<Map<String, Object>> tagsAggregation = (List<Map<String, Object>>) result.get("tags");
        Map<String, Object> resMap = Maps.newHashMap();
        resMap.put("shop", shopModelList);
        resMap.put("category", categoryModelList);
        resMap.put("tags", tagsAggregation);
        return CommonRes.create(resMap);
    }
}
