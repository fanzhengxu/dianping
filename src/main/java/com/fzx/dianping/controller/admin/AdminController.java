package com.fzx.dianping.controller.admin;

import com.fzx.dianping.common.AdminPermission;
import com.fzx.dianping.common.BusinessException;
import com.fzx.dianping.common.EmBusinessError;
import com.fzx.dianping.service.CategoryService;
import com.fzx.dianping.service.SellerServie;
import com.fzx.dianping.service.ShopService;
import com.fzx.dianping.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 22:03 2020/2/12
 */
@Controller("/admin/admin")
@RequestMapping("/admin/admin")
public class AdminController {

    @Value("${admin.email}")
    private String email;

    @Value("${admin.encryptPassword}")
    private String encryptPassword;

    @Autowired
    private HttpServletRequest httpServletRequest;

    public static final String CURRENT_ADMIN_SESSION = "currentAdminSession";

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private SellerServie sellerServie;

    @RequestMapping("/index")
    @AdminPermission
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/admin/admin/index");
        modelAndView.addObject("userCount", userService.countAllUser());
        modelAndView.addObject("shopCount", shopService.countAllShop());
        modelAndView.addObject("categoryCount", categoryService.countAllCategory());
        modelAndView.addObject("sellerCount", sellerServie.countAllSeller());
        modelAndView.addObject("CONTROLLER_NAME", "admin");
        modelAndView.addObject("ACTION_NAME", "index");
        return modelAndView;
    }

    @RequestMapping("/loginPage")
    public ModelAndView loginPage() {
        ModelAndView modelAndView = new ModelAndView("/admin/admin/login");
        return modelAndView;
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestParam(name="email")String email,
                        @RequestParam(name="password")String password ) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户名密码不能为空");
        }
        if(email.equals(this.email) && encodeByMD5(password).equals(this.encryptPassword)){
            //登录成功
            httpServletRequest.getSession().setAttribute(CURRENT_ADMIN_SESSION,email);
            return "redirect:/admin/admin/index";
        }else{
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户名密码错误");
        }

    }

    private String encodeByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(messageDigest.digest(str.getBytes("utf-8")));
    }
}
