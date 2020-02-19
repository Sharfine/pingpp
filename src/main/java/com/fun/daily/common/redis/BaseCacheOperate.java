//package com.ping.daily.common.redis;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * Description: 缓存操作模板
// *
// * @author yugj
// * @date 2018/8/3 17:31
// */
//public abstract class BaseCacheOperate {
//
//    /**
//     * 缓存操作时间阈值 单位毫秒
//     */
//    private static final long CACHE_OPERATE_THRESHOLD_MILLIS_SECOND = 100;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(BaseCacheOperate.class);
//
//    /**
//     * 缓存操作
//     * 用于编写用户操作逻辑,void方法返回null
//     * @return 返回值
//     * @author yugj
//     */
//    public abstract Object userOperate();
//
//    /**
//     * 操作别名 定义操作描述
//     *
//     * @return op alias
//     */
//    public abstract String operateAlias();
//
//    /**
//     * 执行缓存操作
//     * 处理执行操作,统一异常处理,统一日志记录
//     * @param failedValue 失败默认值
//     * @param clazz 返回值类
//     * @param <T>   返回类型
//     * @return 操作结果
//     */
//    public <T> T execute(Object failedValue,Class<T> clazz) {
//
//        Object result = null;
//
//        try {
//            long startTime = System.currentTimeMillis();
//            result = userOperate();
//            long endTime = System.currentTimeMillis();
//
//            long cost = endTime - startTime;
//
//            if (cost > CACHE_OPERATE_THRESHOLD_MILLIS_SECOND) {
//                LOGGER.warn("operate :{}, cost :{} ms, great than threshold :{} ms", operateAlias(), cost,CACHE_OPERATE_THRESHOLD_MILLIS_SECOND);
//            }
//
//        } catch (Exception e) {
//
//            LOGGER.error("redis cluster operate error", e);
//
//            if (null != failedValue && clazz != null) {
//                return (T)failedValue;
//            }
//        }
//
//        if (null == result) {
//            return null;
//        }
//
//        return (T) result;
//    }
//
//}
