package com.fzx.dianping.request;

import javax.validation.constraints.NotBlank;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 21:08 2020/2/14
 */
public class SellerCreateReq {

    @NotBlank(message = "商户名不能为空")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SellerCreateReq(@NotBlank(message = "") String name) {
        this.name = name;
    }
}
