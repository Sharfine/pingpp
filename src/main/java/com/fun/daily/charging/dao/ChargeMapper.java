package com.fun.daily.charging.dao;

import com.fun.daily.charging.model.Goods;
import com.fun.daily.charging.model.OrderInfo;
import org.apache.ibatis.annotations.Param;

public interface ChargeMapper {

    Goods getGoodsByGoodsId(@Param("goodsId") Integer goodsId);

    void addOrderInfo(OrderInfo orderInfo);

    OrderInfo queryOrderInfo(@Param("orderId")String orderId);

    void updateOrderStatus(@Param("orderId")String orderId, @Param("status")Integer status);
}
