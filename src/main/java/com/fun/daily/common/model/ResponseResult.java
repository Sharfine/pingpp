package com.fun.daily.common.model;

import com.fun.daily.common.enums.ResultEnums;

import java.io.Serializable;

/**
 * 返回的基类
 */
public class ResponseResult implements Serializable {

    private String code;

    private String msg;

    private Object data;


    public ResponseResult(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(ResultEnums resultEnums) {
        this.code = resultEnums.getCode();
        this.msg = resultEnums.getMsg();
    }

    public ResponseResult(ResultEnums resultEnums, Object data) {
        this.code = resultEnums.getCode();
        this.msg = resultEnums.getMsg();
        this.data = data;
    }

    public ResponseResult() {
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
