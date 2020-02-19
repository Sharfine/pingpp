package com.fun.daily.charging.service;

import com.fun.daily.charging.model.Goods;
import com.fun.daily.charging.model.OrderInfo;

/**
 *  计费的service
 */
public interface IChargeService {

    Goods getGoodsByGoodsId(Integer goodsId);

    void addOrderInfo(OrderInfo orderInfo);

    OrderInfo queryOrderInfo(String orderId);

    void updateOrderStatus(String orderId, Integer status);
}
