package com.ping.pay.thirdparty.service;

import com.ping.pay.charging.model.OrderInfo;
import com.pingplusplus.model.Charge;

/**
 * ping++ service
 */
public interface PingService {

    /**
     * 对接Ping++ 创建订单
     * @param orderInfo {@link OrderInfo}
     * @return {@link Charge}
     */
    Charge createCharge(OrderInfo orderInfo);
}
