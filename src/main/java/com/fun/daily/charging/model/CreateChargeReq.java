package com.fun.daily.charging.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateChargeReq {

    @NotEmpty
    private String userId;

    @NotNull
    private Integer goodsId;

    @NotEmpty
    private String payChannel;

    @NotEmpty
    private String appChannel;
}
