package com.fun.daily.common.exception;

/**
 * 业务异常类
 */
public class ServiceException extends RuntimeException {

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ServiceException(String errorMessage){
        super(errorMessage);
    }

    public ServiceException(String errorMessage, Throwable t){
        super(errorMessage, t);
    }

    public ServiceException(int code, String errorMessage){
        super(errorMessage);
        this.code = code;
    }
}
