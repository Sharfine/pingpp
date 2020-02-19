package com.fun.daily.common.advice;

import com.fun.daily.common.enums.ResultEnums;
import com.fun.daily.common.exception.ServiceException;
import com.fun.daily.common.model.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  controller统一异常类
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class ExceptionHandlerAdvice {

    /**
     * 处理未捕获的Exception
     * @param e 异常
     * @return 统一响应体
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult handleException(Exception e){
        log.error(e.getMessage(),e);
        return new ResponseResult(ResultEnums.SYSTEM_ERROR.getCode(), ResultEnums.SYSTEM_ERROR.getMsg());
    }

    /**
     * 处理未捕获的RuntimeException
     * @param e 运行时异常
     * @return 统一响应体
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseResult handleRuntimeException(RuntimeException e){
        log.error(e.getMessage(),e);
        return new ResponseResult(ResultEnums.ERROR.getCode(), ResultEnums.ERROR.getMsg());
    }

    /**
     * 处理业务异常BaseException
     * @param e 业务异常
     * @return 统一响应体
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseResult handleBaseException(ServiceException e){
        log.error(e.getMessage(),e);
        return new ResponseResult(ResultEnums.ERROR.getCode(), e.getMessage());
    }
}
