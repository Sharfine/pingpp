package com.fun.daily.charging.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Goods {

    private Integer goodsId;

    private String goodsName;

    private String goodsDes;

    private Integer price;

}
