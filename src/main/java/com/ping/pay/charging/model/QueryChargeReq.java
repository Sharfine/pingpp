package com.ping.pay.charging.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author: cyz
 * @date: 2019/9/6 下午11:51
 * @description:
 */
@Data
public class QueryChargeReq {

    @NotEmpty
    private String userId;

    @NotEmpty
    private String orderId;
}
