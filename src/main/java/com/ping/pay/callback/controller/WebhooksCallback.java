package com.ping.pay.callback.controller;

import com.alibaba.fastjson.JSON;
import com.ping.pay.callback.model.CallbackInfo;
import com.ping.pay.common.enums.ResultEnums;
import com.ping.pay.common.model.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/webhooks")
@Slf4j
public class WebhooksCallback {


    @RequestMapping("/callback")
    @ResponseBody
    public ResponseResult callback(@Valid @RequestBody CallbackInfo callbackInfo){
        log.info("callback info:{}", JSON.toJSONString(callbackInfo));
        switch (callbackInfo.getType()){

            case "charge.succeeded":

                log.info("charge.succeeded");

                break;

            default:
                log.info("other type no deal");
        }


        return new ResponseResult(ResultEnums.SUCCESS.getCode(), "success");
    }
}
