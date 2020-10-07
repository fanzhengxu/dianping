package com.fzx.dianping.common;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 15:36 2020/2/12
 */
public class CommonUtil {

    public static String processErrorString(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return "";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (FieldError fieldError: bindingResult.getFieldErrors()) {
                stringBuilder.append(fieldError.getDefaultMessage() + ",");
            }
            return stringBuilder.substring(0, stringBuilder.length() - 1);
        }
    }
}
