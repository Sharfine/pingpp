package com.fun.daily.charging.controller;

import com.fun.daily.charging.constant.PayContrant;
import com.fun.daily.charging.model.*;
import com.fun.daily.charging.service.IChargeService;
import com.fun.daily.common.exception.ServiceException;
import com.fun.daily.thirdparty.service.PingService;
import com.pingplusplus.model.Charge;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

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

        String orderId = System.currentTimeMillis() + RandomStringUtils.random(5, true, true);
        UserOrder userOrder = UserOrder.builder().orderId(orderId).amount(goods.getPrice()).currency(PayContrant.CURRENCY).channel(req.getPayChannel()).goods(goods).client_ip("192.168.0.1").build();
        OrderInfo orderInfo = OrderInfo.builder().orderId(orderId).goodsId(goods.getGoodsId()).appChannel(req.getPayChannel()).status(0).userId(req.getUserId()).build();

        Charge charge = pingService.createCharge(userOrder);
        if(charge == null){
            throw new ServiceException("创建订单失败,请稍后重试");
        }

        CreateChargeResp createChargeResp = CreateChargeResp.builder().charge(charge).build();
        //创建成功，插入自己的表
        chargeService.addOrderInfo(orderInfo);

        return createChargeResp;
    }

    @RequestMapping("/query")
    @ResponseBody
    public QueryChargeResp queryCharge(@Valid @RequestBody QueryChargeReq req){

        QueryChargeResp queryChargeResp = new QueryChargeResp();
        String userId = req.getUserId();
        String orderId = req.getOrderId();

        OrderInfo orderInfo = chargeService.queryOrderInfo(orderId);
        if(orderInfo == null || !Objects.equals(orderInfo.getUserId(), userId)){
            throw new ServiceException("查无此订单");
        }

        queryChargeResp.setOrderInfo(orderInfo);
        return queryChargeResp;

    }

}
