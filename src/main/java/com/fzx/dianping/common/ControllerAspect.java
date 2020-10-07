package com.fzx.dianping.common;

import com.fzx.dianping.controller.admin.AdminController;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 22:27 2020/2/12
 */
@Aspect
@Configuration
public class ControllerAspect {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Around("execution(* com.fzx.dianping.controller.admin.*.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping))")
    public Object adminControllerBeforeVaildation(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        AdminPermission adminPermission = method.getAnnotation(AdminPermission.class);
        if (adminPermission == null) {
            Object resultObject = joinPoint.proceed();
            return resultObject;
        }
        String email = (String) httpServletRequest.getSession().getAttribute(AdminController.CURRENT_ADMIN_SESSION);
        if (StringUtils.isBlank(email)) {
            if (adminPermission.produceType().equals("text/html")) {
                httpServletResponse.sendRedirect("/admin/admin/loginPage");
                return null;
            } else {
                CommonError commonError = new CommonError(EmBusinessError.ADMIN_SHOULD_LOGIN);
                return CommonRes.create(commonError, "fail");
            }
        } else {
            Object resultObject = joinPoint.proceed();
            return resultObject;
        }
    }
}
