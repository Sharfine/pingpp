package com.fun.daily.callback.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallbackInfo<T> {

    /**
     * 事件对象 id ，由 Ping++ 生成，28 位长度字符串。
     */
    private String id;

    /**
     * 值为 "event"。
     */
    private String object;

    /**
     * 事件是否发生在生产环境。
     */
    private Boolean livemode;

    /**
     * 事件发生的时间。
     */
    private Long created;

    /**
     * 推送未成功的 webhooks 数量。
     */
    private Integer pending_webhooks;

    /**
     * 事件类型，详见事件类型。
     */
    private String type;

    /**
     * API Request ID。值 "null" 表示该事件不是由 API 请求触发的。
     */
    private String request;


    private CallbackData<T> data;


}
