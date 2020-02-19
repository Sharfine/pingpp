package com.fun.daily.callback.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: cyz
 * @date: 2019/9/6 下午10:41
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallbackData<T> {

    private T object;

}
