package com.fun.daily.thirdparty.service;

import com.fun.daily.charging.model.UserOrder;
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
