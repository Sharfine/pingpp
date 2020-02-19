//package com.ping.daily.common.aspect;
//
//import com.ping.daily.common.redis.CacheManager;
//import com.ping.daily.common.redis.constant.RedisCacheConstant;
//
///**
// * 切面工具类
// * @author yugj 18/1/25 21:22.
// * @date 18/1/25 21:22.
// */
//public class ServiceAspectUtil {
//
//    /**
//     * 取缓存key值
//     * @param className 类名
//     * @param methodName 方法名
//     * @param params 参数
//     * @return redis key
//     */
//    public static String getServiceCacheKey(String className, String methodName, Object[] params) {
//
//        StringBuffer buffer = new StringBuffer();
//
//        buffer.append(RedisCacheConstant.SERVICE_CACHE_PREFIX)
//                .append(className).append("-").append(methodName);
//
//        if (params == null) {
//            return buffer.toString();
//        }
//
//        for (Object param : params) {
//            if (null == param) {
//                buffer.append("-");
//            } else {
//                buffer.append("-").append(param.toString());
//            }
//        }
//
//        return buffer.toString();
//    }
//
//    /**
//     * 删除service切面缓存
//     * @param serviceClass
//     * @param methodName
//     * @param params
//     */
//    public static void delServiceCache(Class<?> serviceClass, String methodName, Object... params){
//        if(params == null){
//            params = new Object[]{};
//        }
//        String cacheKey = getServiceCacheKey(serviceClass.getSimpleName(), methodName, params);
//        CacheManager.delCache(cacheKey);
//    }
//
//}
