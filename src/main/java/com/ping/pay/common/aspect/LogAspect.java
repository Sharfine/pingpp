package com.ping.pay.common.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Before("within(com.ping.pay.*.controller.*)")
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

    @AfterReturning(value = "within(com.ping.pay.*.controller.*)", returning = "data")
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
