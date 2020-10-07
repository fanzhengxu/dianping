package com.fzx.dianping.controller.admin;

import com.fzx.dianping.common.*;
import com.fzx.dianping.model.SellerModel;
import com.fzx.dianping.request.PageQuery;
import com.fzx.dianping.request.SellerCreateReq;
import com.fzx.dianping.service.SellerServie;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 20:34 2020/2/14
 */
@Controller("/admin/seller")
@RequestMapping("/admin/seller")
public class SellerController {

    @Autowired
    private SellerServie sellerServie;

    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index(PageQuery pageQuery){
        if (pageQuery.getPage() == null && pageQuery.getSize() == null) {
            pageQuery = new PageQuery(1, 20);
        }
        PageHelper.startPage(pageQuery.getPage(), pageQuery.getSize());
        List<SellerModel> sellerModelList = sellerServie.selectAll();
        PageInfo<SellerModel> sellerModelPageInfo = new PageInfo<>(sellerModelList);
        ModelAndView modelAndView = new ModelAndView("/admin/seller/index");
        modelAndView.addObject("data",sellerModelPageInfo);
        modelAndView.addObject("CONTROLLER_NAME","seller");
        modelAndView.addObject("ACTION_NAME","index");
        return modelAndView;
    }

    @RequestMapping("/createPage")
    @AdminPermission
    public ModelAndView createPage() {
        ModelAndView modelAndView = new ModelAndView("/admin/seller/create.html");
        modelAndView.addObject("CONTROLLER_NAME","seller");
        modelAndView.addObject("ACTION_NAME","index");
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @AdminPermission
    public String create(@Valid SellerCreateReq sellerCreateReq, BindingResult bindingResult) throws BusinessException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }
        SellerModel sellerModel = new SellerModel();
        sellerModel.setName(sellerCreateReq.getName());
        sellerServie.create(sellerModel);
        return "redirect:/admin/seller/index";
    }

    @RequestMapping(value = "down", method = RequestMethod.POST)
    @AdminPermission
    @ResponseBody
    public CommonRes down(@RequestParam(value = "id") Integer id) throws BusinessException {
        SellerModel sellerModel = sellerServie.changeStatus(id, 1);
        return CommonRes.create(sellerModel);
    }

    @RequestMapping(value = "up", method = RequestMethod.POST)
    @AdminPermission
    @ResponseBody
    public CommonRes up(@RequestParam(value = "id") Integer id) throws BusinessException {
        SellerModel sellerModel = sellerServie.changeStatus(id, 0);
        return CommonRes.create(sellerModel);
    }
}
