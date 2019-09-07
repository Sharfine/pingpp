package com.ping.pay.charging.model;

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
public class UserOrder {

    /**
     * 订单id 自己的
     */
    private String orderId;

    /**
     * 商品
     */
    private Goods goods;

    /**
     * 3 位 ISO 货币代码，小写字母，人民币为 cny。使用跨境渠道参考 跨境渠道 currency 说明 ；使用 isv_lite 渠道只支持 cny,hkd 其中的一个值。
     */
    private String currency;

    /**
     * 订单总金额（必须大于 0），单位为对应币种的最小货币单位，人民币为分。如订单总金额为 1 元，amount 为 100。
     */
    private Integer amount;


    /**
     * 支付使用的第三方支付渠道。参考 支付渠道属性值。
     * 见：https://www.pingxx.com/api/%E6%94%AF%E4%BB%98%E6%B8%A0%E9%81%93%E5%B1%9E%E6%80%A7%E5%80%BC
     */
    private String channel;

    /**
     * 发起支付请求客户端的 IP 地址，支持 IPv4、IPv6 格式。
     */
    private String client_ip;
}
