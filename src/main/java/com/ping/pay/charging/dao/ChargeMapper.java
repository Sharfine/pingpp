package com.ping.pay.charging.dao;

import com.ping.pay.charging.model.Goods;
import com.ping.pay.charging.model.OrderInfo;
import org.apache.ibatis.annotations.Param;

public interface ChargeMapper {

    Goods getGoodsByGoodsId(@Param("goodsId") Integer goodsId);

    void addOrderInfo(OrderInfo orderInfo);

    OrderInfo queryOrderInfo(@Param("orderId")String orderId);

    void updateOrderStatus(@Param("orderId")String orderId, @Param("status")Integer status);
}
