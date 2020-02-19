package com.fun.daily.charging.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {

    /**
     * 订单id 自己的
     */
    private String orderId;

    /**
     * 商品
     */
    private Integer goodsId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * app渠道
     */
    private String appChannel;

    /**
     * 状态
     */
    private Integer status;
}
