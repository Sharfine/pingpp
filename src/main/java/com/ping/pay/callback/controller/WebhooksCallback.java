package com.ping.pay.callback.controller;

import com.alibaba.fastjson.JSON;
import com.ping.pay.callback.model.CallbackCharge;
import com.ping.pay.callback.model.CallbackInfo;
import com.ping.pay.charging.service.IChargeService;
import com.ping.pay.common.enums.ResultEnums;
import com.ping.pay.common.model.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/webhooks")
@Slf4j
public class WebhooksCallback {

    @Autowired
    private IChargeService chargeService;

    @RequestMapping("/callback")
    @ResponseBody
    public ResponseResult callback(@Valid @RequestBody CallbackInfo<CallbackCharge> callbackInfo){
        log.info("callback info:{}", JSON.toJSONString(callbackInfo));
        switch (callbackInfo.getType()){

            case "charge.succeeded":

                CallbackCharge charge = callbackInfo.getData().getObject();
                String orderId = charge.getOrderNo();
                Integer amount = charge.getAmount();
                boolean paid = charge.getPaid();
                //是否支付
                if(paid){
                    chargeService.updateOrderStatus(orderId, 1);
                    log.info("orderId:{} is charge succeeded", orderId);
                }

                break;

            default:
                log.info("other type no deal");
        }


        return new ResponseResult(ResultEnums.SUCCESS.getCode(), "success");
    }
}
