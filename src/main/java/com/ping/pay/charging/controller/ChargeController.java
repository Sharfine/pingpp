package com.ping.pay.charging.controller;

import com.ping.pay.charging.model.CreateChargeResp;
import com.ping.pay.charging.model.Goods;
import com.ping.pay.charging.model.OrderInfo;
import com.ping.pay.thirdparty.service.PingService;
import com.pingplusplus.model.Charge;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/charge")
public class ChargeController {

    @Autowired
    private PingService pingService;

    @RequestMapping("/create")
    @ResponseBody
    public CreateChargeResp createCharge(HttpServletRequest request){

        String orderId = new Date().getTime() + RandomStringUtils.random(5, true, true);
        Goods goods = Goods.builder().goodsDes("测试商品描述").goodsName("测试商品名称").goodsId(1001).build();
        OrderInfo orderInfo = OrderInfo.builder().orderId(orderId).amount(100).currency("cny").channel("wx").goods(goods).client_ip("192.168.0.1").build();

        Charge charge = pingService.createCharge(orderInfo);
        CreateChargeResp createChargeResp = CreateChargeResp.builder().charge(charge).build();
        return createChargeResp;
    }
}
