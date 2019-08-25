package com.ping.pay.common.enums;

/**
 *  统一返回的枚举
 */
public enum ResultEnums {

    SUCCESS("0", "请求成功"),
    ERROR("1", "业务逻辑错误"),
    SYSTEM_ERROR("9", "系统异常");

    private String code;
    private String msg;

    ResultEnums(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
