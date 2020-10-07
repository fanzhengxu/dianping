package com.fzx.dianping.common;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 19:59 2020/1/30
 */
public class CommonRes {

    //返回结果，"success"或"fail"
    private String status;

    //status=success时，返回json数据
    //status=fail时，返回通用格式错误
    private Object data;

    public static CommonRes create(Object result) {
        return CommonRes.create(result, "success");
    }

    public static CommonRes create(Object result, String status) {
        CommonRes commonRes = new CommonRes();
        commonRes.setData(result);
        commonRes.setStatus(status);
        return commonRes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
