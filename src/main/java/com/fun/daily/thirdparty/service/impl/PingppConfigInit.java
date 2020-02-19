package com.fun.daily.thirdparty.service.impl;

import com.pingplusplus.Pingpp;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 启动程序配置ping++ appid
 */
@Component
public class PingppConfigInit implements InitializingBean {

    @Value("${pingpp.appId}")
    private String appId;

    @Value("${pingpp.appKey}")
    private String appKey;

    @Value("${pingpp.privateKey.path}")
    private String privateKeyPath;

    @Override
    public void afterPropertiesSet(){
        Pingpp.appId = appId;
        Pingpp.apiKey = appKey;
        Pingpp.privateKeyPath = privateKeyPath;
    }
}
