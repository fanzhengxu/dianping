package com.fzx.dianping.common;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 20:13 2020/1/30
 */
public enum EmBusinessError {

    //通用错误
    NO_OBJECT_FOUND(10001, "请求对象不存在"),
    UNKNOWN_ERROR(10002, "未知错误"),
    NO_HANDLER_FOUND(10003, "找不到对应路径"),
    BIND_EXCEPTION_ERROR(10004, "请求参数错误"),
    PARAMETER_VALIDATION_ERROR(10005, "请求参数校验失败"),

    //用户服务相关错误
    REGISTER_DUP_FAIL(20001, "用户已存在"),
    LOGIN_FAIL(20002, "手机号或密码错误"),

    //admin相关错误
    ADMIN_SHOULD_LOGIN(30001, "管理员需要先登录"),
    CATEGORY_NAME_DUPLICATED(30002, "品类名已存在");

    private Integer errCode;

    private String errMsg;

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    EmBusinessError(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }}
