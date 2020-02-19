//package com.ping.daily.common.redis.annotation;
//
//import java.lang.annotation.*;
//
///**
// * 缓存注解类
// * @author xiangheng.song
// * @date 2017/10/26 12:20
// */
//@Target({ElementType.METHOD})
//@Retention(RetentionPolicy.RUNTIME)
//@Inherited
//public @interface NeedCache {
//
//    /**
//     * 固定时间缓存 单位分钟
//     * Deprecated 优先使用 rangeFrom rangeTo 配置一个缓存时间范围，使缓存随机失效，减少集中命中缓存情况
//     * @return
//     */
//    @Deprecated
//    int value() default 0;
//
//    /**
//     * 时间范围开始 单位分钟
//     * @return
//     */
//    int rangeFrom() default 0;
//
//    /**
//     * 时间范围结束 单位分钟
//     * @return
//     */
//    int rangeTo() default 0;
//
//}
