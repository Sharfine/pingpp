package com.ping.pay.charging.service;

import com.ping.pay.charging.model.Goods;
import com.ping.pay.charging.model.OrderInfo;

public interface IChargeService {

    Goods getGoodsByGoodsId(Integer goodsId);

    void addOrderInfo(String userId, OrderInfo orderInfo);
}
