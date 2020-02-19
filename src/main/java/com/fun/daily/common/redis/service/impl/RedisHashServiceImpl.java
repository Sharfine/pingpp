package com.fun.daily.common.redis.service.impl;

import com.fun.daily.common.redis.service.IRedisHashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author: cyz
 * @date: 2019/9/6 下午9:41
 * @description:
 */
@Service
public class RedisHashServiceImpl implements IRedisHashService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * HashMap中的是否存在键值为key的hash结构
     *
     * @param key 键值
     * @return 返回 存在true，不存在false
     * @author cxg
     * @date 20181228 14:23:37
     * @modify 20181228 cxg v1.0 创建
     * @since v1.0
     */
    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 根据给定键值key，获取Hash结构中的key为name对应的value.
     *
     * @param key 键值
     * @param name Hash结构中的key值
     * @return 返回 Hash结构中的key值为name对应的value
     * @author cxg
     * @date 20181228 14:22:38
     * @modify 20181228 cxg v1.0 创建
     * @since v1.0
     */
    @Override
    public String getRedisHash(String key, String name) {
        return redisTemplate.opsForHash().get(key, name).toString();
    }

    /**
     * 向指定键值key中新增一个Hash结构键值对，key为name，value为value
     *
     * @param key 键值
     * @param name Hash结构中的键
     * @param value Hash结构中的键对应的值
     * @author cxg
     * @date 20181228 14:22:38
     * @modify 20181228 cxg v1.0 创建
     * @since v1.0
     */
    @Override
    public void addRedisHash(String key, String name, String value) {
        redisTemplate.opsForHash().put(key, name,value);
    }

    /**
     * 删除Redis中键值为key的hash结构
     *
     * @param key 键值
     * @return 返回 boolean 删除结果，成功true，失败false
     * @author cxg
     * @date 20181228 14:52:14
     * @modify 20181228 cxg v1.0 创建
     * @since v1.0
     */
    @Override
    public Boolean delRedisHash(String key) {
        return redisTemplate.delete(key);
    }
}
