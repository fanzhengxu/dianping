package com.fzx.dianping.controller;

import com.alibaba.fastjson.JSONObject;
import com.fzx.dianping.common.CommonRes;
import com.fzx.dianping.model.CategoryModel;
import com.fzx.dianping.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 13:23 2020/2/15
 */
@Controller("/category")
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/list")
    @ResponseBody
    public CommonRes list() {
        List<CategoryModel> categoryModelList = categoryService.selectAll();
        return CommonRes.create(categoryModelList);
    }
}
