//package com.ping.pay.common.aspect;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.parser.ParserConfig;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.ping.pay.common.redis.CacheManager;
//import com.ping.pay.common.redis.annotation.NeedCache;
//import com.ping.pay.common.redis.constant.RedisCacheConstant;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.RandomUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.core.annotation.AnnotationUtils;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//import java.lang.reflect.Type;
//
///**
// * Description:need cache server 缓存拦截实现
// * 拦截cn.migudm.common.cache.redis.annotations.NeedCache注解方法
// * 请求该类 在redis判断是否含类 方法名 参数名组成的key,如果含 直接返回
// * 不含则继续执行
// * Created by yugj on 18/1/25 16:49.
// * @author yugj
// */
//@Aspect
//@Component
//@Slf4j
//public class NeedCacheServiceAspect extends NeedCacheHelper {
//
//    private static final String CACHE_LOCK_PREFIX = "CLP-";
//    private static final String CACHE_LOCK_VALUE = "1";
//
//    /**
//     * 切面入口 只连接NeedCache注解类
//     *
//     * @param call call
//     * @return resp
//     * @throws Throwable
//     */
//    @Around("@annotation(com.ping.pay.common.redis.annotation.NeedCache)")
//    public Object execute(final ProceedingJoinPoint call) throws Throwable {
//
//        MethodSignature methodSignature = (MethodSignature) call.getSignature();
//        Method method = methodSignature.getMethod();
//        long startTime = System.currentTimeMillis();
//
//        NeedCache needCache = AnnotationUtils.findAnnotation(method, NeedCache.class);
//
//        String cacheKey = ServiceAspectUtil.getServiceCacheKey(call.getTarget().getClass().getSimpleName(), method.getName(), call.getArgs());
//
//        //初次进入取缓存值
//        long getCacheStart = System.currentTimeMillis();
//        String cacheJsonStr = CacheManager.getCache(cacheKey);
//        long getCacheEnd = System.currentTimeMillis();
//        log.debug("get cache:{},cost:{}", cacheKey, (getCacheEnd - getCacheStart));
//
//        //缓存存在直接返回
//        if (StringUtils.isNoneBlank(cacheJsonStr)) {
//            log.debug("cache:{} exists,get result from cache");
//            return parseCacheValue(cacheJsonStr, method.getGenericReturnType());
//        }
//
//        //无缓存情况
//        //缓存锁实现 缓存锁存在则等待300毫秒
//        String lockKey = CACHE_LOCK_PREFIX + cacheKey;
//        String lockValue = CacheManager.getCache(lockKey);
//
//        boolean lockOperation = false;
//        if (CACHE_LOCK_VALUE.equals(lockValue)) {
//            log.debug("lock key:{} exists,sleep 300ms",lockKey);
//
//            Thread.sleep(300L);
//
//            String cacheValue = CacheManager.getCache(cacheKey);
//            if (StringUtils.isNoneBlank(cacheValue)) {
//                log.debug("wait lock 300ms,and get result from cache");
//                return parseCacheValue(cacheValue, method.getGenericReturnType());
//            }
//
//            log.info("cache:{},wait lock 300ms,can not get result from cache",cacheKey);
//
//        } else {
//            //上锁
//            log.debug("lock key:{} not exists,set lock", lockKey);
//
//            CacheManager.setCacheSecond(lockKey, CACHE_LOCK_VALUE, 10);
//            lockOperation = true;
//        }
//
//
//        //业务执行
//        long proceedStart = System.currentTimeMillis();
//        Object result = call.proceed();
//        long proceedEnd = System.currentTimeMillis();
//        log.debug("service data proceed,cache key:{}, proceed cost:{}", cacheKey, (proceedEnd - proceedStart));
//
//        //设置返回值到缓存
//        int expireSecond = getCacheTimeSecond(needCache);
//
//        CacheManager.setCacheSecond(cacheKey, JSON.toJSONString(result, SerializerFeature.WriteClassName), expireSecond);
//        long setCacheEnd = System.currentTimeMillis();
//        log.debug("set cache:{},cost:{}", cacheKey, (setCacheEnd - proceedEnd));
//
//        //大于指定时延的做打印输出
//        long endTime = System.currentTimeMillis();
//        long cost = endTime - startTime;
//        if (cost >= noCacheThresholdMillisSecond) {
//            log.info("cache key:{},process and set cache cost {}ms", cacheKey, cost);
//        }
//
//        //解锁
//        if (lockOperation) {
//            CacheManager.delCache(lockKey);
//        }
//
//        return result;
//
//    }
//
//    /**
//     * 取缓存时间
//     * 设置固定缓存取固定时间
//     * 设置缓存范围取范围内随机缓存时间
//     * @param needCache cache注解
//     * @return 缓存时间
//     */
//    private int getCacheTimeSecond(NeedCache needCache) {
//
//        int value = needCache.value();
//
//        if (value != 0) {
//            return value * 60;
//        }
//
//        if (needCache.rangeFrom() == 0) {
//            throw new IllegalArgumentException("rangeFrom can not be null");
//        }
//
//        if (needCache.rangeTo() == 0) {
//            throw new IllegalArgumentException("rangeTo can not be null");
//        }
//        if (needCache.rangeTo() <= needCache.rangeFrom()) {
//            throw new IllegalArgumentException("error,rangeTo < rangeFrom");
//        }
//
//        if (needCache.rangeFrom() < 1) {
//            throw new IllegalArgumentException("error,rangeForm < 1");
//        }
//
//        int max = needCache.rangeTo() * 60;
//        int min = needCache.rangeFrom() * 60;
//
//        return RandomUtils.nextInt(min, max);
//
//    }
//
//    /**
//     * 转换缓存值
//     * @param cacheValue 缓存查询结果
//     * @param type 返回类型
//     * @return 返回对象
//     */
//    private Object parseCacheValue(String cacheValue,Type type) {
//
//        //缓存存在，只是内容为null。这里也返回null
//        if (RedisCacheConstant.VALUE_IS_NULL.equals(cacheValue)) {
//            return null;
//        }
//
//        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
//        return JSON.parseObject(cacheValue, type);
//
//    }
//
//}
