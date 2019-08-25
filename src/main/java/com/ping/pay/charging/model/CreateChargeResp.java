package com.ping.pay.charging.model;

import com.pingplusplus.model.Charge;
import lombok.Builder;
import lombok.Data;

/**
 * 创建计费
 */
@Data
@Builder
public class CreateChargeResp {

    private Charge charge;

}
