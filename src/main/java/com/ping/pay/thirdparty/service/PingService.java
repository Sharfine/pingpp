package com.ping.pay.thirdparty.service;

import com.ping.pay.charging.model.OrderInfo;
import com.ping.pay.charging.model.UserOrder;
import com.pingplusplus.model.Charge;

/**
 * ping++ service
 */
public interface PingService {

    /**
     * 对接Ping++ 创建订单
     * @param userOrder {@link UserOrder}
     * @return {@link Charge}
     */
    Charge createCharge(UserOrder userOrder);
}
