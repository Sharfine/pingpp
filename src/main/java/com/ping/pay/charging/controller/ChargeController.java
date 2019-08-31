package com.ping.pay.charging.controller;

import com.ping.pay.charging.contrant.PayContrant;
import com.ping.pay.charging.model.CreateChargeReq;
import com.ping.pay.charging.model.CreateChargeResp;
import com.ping.pay.charging.model.Goods;
import com.ping.pay.charging.model.OrderInfo;
import com.ping.pay.charging.service.IChargeService;
import com.ping.pay.common.exception.ServiceException;
import com.ping.pay.thirdparty.service.PingService;
import com.pingplusplus.model.Charge;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/charge")
public class ChargeController {

    @Autowired
    private PingService pingService;

    @Autowired
    private IChargeService chargeService;

    @RequestMapping("/create")
    @ResponseBody
    public CreateChargeResp createCharge(@Valid @RequestBody CreateChargeReq req){

        Goods goods = chargeService.getGoodsByGoodsId(req.getGoodsId());
        if(goods == null){
            throw new ServiceException("查无此商品");
        }

        String orderId = new Date().getTime() + RandomStringUtils.random(5, true, true);
        OrderInfo orderInfo = OrderInfo.builder().orderId(orderId).amount(goods.getPrice()).currency(PayContrant.CURRENCY).channel(req.getPayChannel()).goods(goods).client_ip("192.168.0.1").build();

        Charge charge = pingService.createCharge(orderInfo);
        if(charge == null){
            throw new ServiceException("创建订单失败,请稍后重试");
        }

        CreateChargeResp createChargeResp = CreateChargeResp.builder().charge(charge).build();
        //创建成功，插入自己的表
        chargeService.addOrderInfo(req.getUserId(), orderInfo);

        return createChargeResp;
    }
}
