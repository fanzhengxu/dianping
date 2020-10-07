package com.fzx.dianping.request;

import javax.validation.constraints.NotBlank;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 16:19 2020/2/12
 */
public class LoginReq {

    @NotBlank(message = "手机号不能为空")
    private String telphone;

    @NotBlank(message = "密码不能为空")
    private String password;

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginReq(@NotBlank(message = "手机号不能为空") String telphone, @NotBlank(message = "密码不能为空") String password) {
        this.telphone = telphone;
        this.password = password;
    }
}
