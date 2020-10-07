package com.fzx.dianping.controller.admin;

import com.fzx.dianping.common.AdminPermission;
import com.fzx.dianping.common.BusinessException;
import com.fzx.dianping.common.CommonUtil;
import com.fzx.dianping.common.EmBusinessError;
import com.fzx.dianping.model.CategoryModel;
import com.fzx.dianping.request.CategoryCreateReq;
import com.fzx.dianping.request.PageQuery;
import com.fzx.dianping.service.CategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 13:03 2020/2/15
 */
@Controller("/admin/category")
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index(PageQuery pageQuery){
        if (pageQuery.getPage() == null && pageQuery.getSize() == null) {
            pageQuery = new PageQuery(1, 20);
        }
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getSize());
        List<CategoryModel> categoryModelList = categoryService.selectAll();
        PageInfo<CategoryModel> categoryModelPageInfo = new PageInfo<>(categoryModelList);
        ModelAndView modelAndView = new ModelAndView("/admin/category/index");
        modelAndView.addObject("data",categoryModelPageInfo);
        modelAndView.addObject("CONTROLLER_NAME","category");
        modelAndView.addObject("ACTION_NAME","index");
        return modelAndView;
    }

    @RequestMapping("/createPage")
    @AdminPermission
    public ModelAndView createPage() {
        ModelAndView modelAndView = new ModelAndView("/admin/category/create.html");
        modelAndView.addObject("CONTROLLER_NAME","category");
        modelAndView.addObject("ACTION_NAME","index");
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @AdminPermission
    public String create(@Valid CategoryCreateReq categoryCreateReq, BindingResult bindingResult) throws BusinessException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setName(categoryCreateReq.getName());
        categoryModel.setIconUrl(categoryCreateReq.getIconUrl());
        categoryModel.setSort(categoryCreateReq.getSort());
        categoryService.create(categoryModel);
        return "redirect:/admin/category/index";
    }
}
