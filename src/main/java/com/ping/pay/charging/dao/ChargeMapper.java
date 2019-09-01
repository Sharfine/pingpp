package com.ping.pay.charging.dao;

import com.ping.pay.charging.model.Goods;
import org.apache.ibatis.annotations.Param;

public interface ChargeMapper {

    Goods getGoodsByGoodsId(@Param("goodsId") Integer goodsId);

    void addOrderInfo(@Param("orderId") String orderId, @Param("userId") String userId, @Param("goodsId") Integer goodsId);
}
