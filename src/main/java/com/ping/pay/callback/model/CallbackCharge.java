package com.ping.pay.callback.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.pingplusplus.model.ChargeRefundCollection;
import lombok.Data;

import java.util.Map;

/**
 * @author: cyz
 * @date: 2019/9/7 上午12:15
 * @description:
 */
@Data
public class CallbackCharge {

    private String id;

    private String object;

    private Long created;

    private Boolean livemode;

    private Boolean paid;

    private Boolean refunded;

    private Boolean reversed;

    private Object app;

    private String channel;

    @JSONField(name = "order_no")
    private String orderNo;

    @JSONField(name = "client_ip")
    private String clientIp;

    private Integer amount;

    @JSONField(name = "amount_settle")
    private Integer amountSettle;

    private String currency;

    private String subject;

    private String body;

    @JSONField(name = "time_paid")
    private Long timePaid;

    @JSONField(name = "time_expire")
    private Long timeExpire;

    @JSONField(name = "time_settle")
    private Long timeSettle;

    @JSONField(name = "transaction_no")
    private String transactionNo;

    private ChargeRefundCollection refunds;

    @JSONField(name = "amount_refunded")
    private Integer amountRefunded;

    @JSONField(name = "failure_code")
    private String failureCode;

    @JSONField(name = "failure_msg")
    private String failureMsg;

    private Map<String, Object> metadata;

    private Map<String, Object> credential;

    private Map<String, Object> extra;

    private String description;
}
