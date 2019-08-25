package com.ping.pay.charging.model;

import lombok.Builder;
import lombok.Data;

/**
 * 商品
 */
@Data
@Builder
public class Goods {

    private Integer goodsId;

    private String goodsName;

    private String goodsDes;

}
