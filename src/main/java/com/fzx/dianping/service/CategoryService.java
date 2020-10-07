package com.fzx.dianping.service;

import com.fzx.dianping.common.BusinessException;
import com.fzx.dianping.model.CategoryModel;

import java.util.List;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 12:45 2020/2/15
 */
public interface CategoryService {

    CategoryModel create(CategoryModel categoryModel) throws BusinessException;

    CategoryModel get(Integer id);

    List<CategoryModel> selectAll();

    Integer countAllCategory();
}
