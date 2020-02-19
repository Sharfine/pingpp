package com.fun.daily.common.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Before("within(com.ping.daily.*.controller.*)")
    public void before(JoinPoint joinPoint){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String uri = request.getRequestURI();
        String requestId =  RandomStringUtils.random(16, true, true);
        request.setAttribute("requestId", requestId);
        request.setAttribute("uri", uri);
        request.setAttribute("startTime", System.currentTimeMillis());
        log.info("【tradeId】{} 【requestUri】{} 【params】{}", requestId, uri, JSON.toJSONString(joinPoint.getArgs()));
    }

    @AfterReturning(value = "within(com.ping.daily.*.controller.*)", returning = "data")
    public void after(JoinPoint joinPoint, Object data){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String requestId = (String) request.getAttribute("requestId");
        String uri = (String) request.getAttribute("uri");
        long startTime = (long) request.getAttribute("startTime");
        long cost = System.currentTimeMillis() - startTime;
        log.info("【tradeId】{} 【requestUri】{} 【return】{} 【cost】{}ms", requestId, uri, JSON.toJSONString(data), cost);
    }

}
