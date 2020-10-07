package com.fzx.dianping.common;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 20:27 2020/1/30
 */
public class BusinessException extends Exception {

    private CommonError commonError;

    public BusinessException(EmBusinessError emBusinessError) {
        super();
        this.commonError = new CommonError(emBusinessError);
    }

    public BusinessException(EmBusinessError emBusinessError, String errMessage) {
        super();
        this.commonError = new CommonError(emBusinessError);
        this.commonError.setErrMsg(errMessage);
    }

    public CommonError getCommonError() {
        return commonError;
    }

    public void setCommonError(CommonError commonError) {
        this.commonError = commonError;
    }
}
