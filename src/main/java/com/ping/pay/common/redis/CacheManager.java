//package com.ping.pay.common.redis;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.util.IOUtils;
//import com.ping.pay.common.redis.constant.RedisCacheConstant;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.util.CollectionUtils;
//import redis.clients.jedis.*;
//import redis.clients.jedis.params.sortedset.ZIncrByParams;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//
///**
// * Redis缓存管理类，提供静态方法控制缓存</br>
// * 请参考官方地址查看命名说明:https://redis.io/commands/{command} </br>
// * eg:https://redis.io/commands/get </br>
// *
// * @author fbzheng 2016年1月23日19:14:11
// * @author yugj
// */
//@Slf4j
//public class CacheManager {
//
//    /**
//     * 不暴露
//     */
//    private CacheManager() {
//
//    }
//
//    /**
//     * 保护JedisClusterClient ,暂时不暴露
//     * @return 返回redis cluster
//     */
//    private static JedisCluster getCluster() {
//        return JedisClusterClient.getResource();
//    }
//
//    /**
//     * 从缓存中获取内容
//     *
//     * @param key 缓存key
//     * @return 缓存值
//     */
//    public static String getCache(final String key) {
//
//        BaseCacheOperate cacheOperate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().get(key);
//            }
//
//            @Override
//            public String operateAlias() {
//                return "get " + key;
//            }
//        };
//        return cacheOperate.execute(null, String.class);
//
//    }
//
//
//    /**
//     * Remove the specified members from the set stored at key. Specified members that are not a member of this set are ignored. If key does not exist, it is treated as an empty set and this command returns 0.
//     * An error is returned when the value stored at key is not a set.
//     * @param key operateAlias
//     * @param member member
//     * @return nteger reply: the number of members that were removed from the set, not including non existing members.
//     */
//    public static Long srem(final String key, final String... member) {
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().srem(key, member);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Long.class);
//    }
//
//    /**
//     * 判断缓存中是否存在改Key
//     * @param key 缓存key
//     * @return 是否存在 false-不存在 true-存在
//     */
//    public static boolean keyIsExists(String key) {
//
//        BaseCacheOperate cacheOperate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().exists(key);
//            }
//            @Override
//            public String operateAlias() {
//                return "exists " + key;
//            }
//        };
//
//        return cacheOperate.execute(false,Boolean.class);
//
//    }
//
//    /**
//     * 限速器操作：自增操作，返回是否允许继续操作
//     *
//     * @param key           自增器名称
//     * @param speed         速度：限制分钟数的速度
//     * @param sectionMinute 限制的时间分钟数：
//     * @return true: 还可进行操作，false：不可进行操作
//     */
//    public static boolean speedLimiter(final String key, long speed, int sectionMinute) {
//
//        BaseCacheOperate cacheOperate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                String finalKey = RedisCacheConstant.SPEEDLIMITER_PREFIX + key;
//
//                long num = getCluster().incr(finalKey);
//                //设置超时时间
//                if (num < 3L) {
//                    getCluster().expire(finalKey, sectionMinute * 60);
//                }
//                return num <= speed;
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return cacheOperate.execute(false,Boolean.class);
//    }
//
//    /**
//     * 限速器操作：自增操作，返回是否允许继续操作
//     *
//     * @param key     自增器名称
//     * @param speed   速度：限制秒数的速度
//     * @param seconds 限制的时间：秒数：
//     * @return true: 还可进行操作，false：不可进行操作
//     */
//    public static boolean speedLimiterSeconds(final String key, long speed, int seconds) {
//
//        BaseCacheOperate cacheOperate = new BaseCacheOperate() {
//
//            @Override
//            public Object userOperate() {
//
//                String targetKey = RedisCacheConstant.SPEEDLIMITER_PREFIX + key;
//                long num = getCluster().incr(targetKey);
//                //设置超时时间
//                if (num < 3L) {
//                    getCluster().expire(targetKey, seconds);
//                }
//                return num <= speed;
//            }
//
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return cacheOperate.execute(false, Boolean.class);
//    }
//
//    public static boolean speedLimiterPerSecond(final String key, long speed) {
//
//        BaseCacheOperate cacheOperate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                String targetKey = RedisCacheConstant.SPEEDLIMITER_PREFIX + key;
//                if (!getCluster().exists(targetKey)) {
//                    getCluster().setex(targetKey, 1, "0");
//                }
//                return getCluster().incr(targetKey) <= speed;
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return cacheOperate.execute(false, Boolean.class);
//    }
//
//    /**
//     * 缓存删除操作
//     *
//     * @param key 缓存key
//     * @return 操作返回值
//     */
//    public static Long delCache(String key) {
//
//        if (StringUtils.isEmpty(key)) {
//            return 0L;
//        }
//
//        BaseCacheOperate cacheOperate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().del(key);
//            }
//            @Override
//            public String operateAlias() {
//                return "del " + key;
//            }
//        };
//
//        return cacheOperate.execute(0L, Long.class);
//    }
//
//    /**
//     * 设置缓存
//     *
//     * @param key key
//     * @param value value
//     * @param expireMinute expireMinute
//     */
//    public static void setCache(String key, String value, int expireMinute) {
//        setCacheSecond(key, value, expireMinute * 60);
//    }
//
//    /**
//     * 设置缓存
//     *
//     * @param key key
//     * @param value value
//     * @param expireSecond expireSecond
//     */
//    public static void setCacheSecond(String key,String value, int expireSecond) {
//
//
//        if (StringUtils.isEmpty(key)) {
//            return;
//        }
//
//        if (expireSecond < 1) {
//            log.warn("expire second lt 1");
//            return;
//        }
//
//        BaseCacheOperate cacheOperate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                String targetValue = value;
//                //空置的标记，在getCache调用的地方需要记录这个是空值
//                if (null == targetValue) {
//                    targetValue = RedisCacheConstant.VALUE_IS_NULL;
//                }
//                getCluster().setex(key, expireSecond, targetValue);
//                return null;
//            }
//            @Override
//            public String operateAlias() {
//                return "set " + key;
//            }
//        };
//
//        cacheOperate.execute(null, null);
//    }
//
//    /**
//     * 添加缓存（防刷key校验）
//     *
//     * @param key key
//     * @param expireMinute 超时分钟
//     * @param value value
//     * @return 是否添加成功
//     */
//    public static boolean safeAddCache(String key, int expireMinute, String value) {
//        return safeAddCacheBySeconds(key, expireMinute * 60, value);
//    }
//
//    /**
//     * 参数为秒
//     *
//     * @param key key
//     * @param expireSeconds 超时秒数
//     * @param value value
//     * @return 是否成功 true-成功 false-失败
//     */
//    public static boolean safeAddCacheBySeconds(String key, int expireSeconds, String value) {
//
//        if (StringUtils.isEmpty(key)) {
//            return false;
//        }
//
//        String targetKey = RedisCacheConstant.SAFE_PREFIX + key;
//
//        BaseCacheOperate cacheOperate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                if (getCluster().setnx(targetKey, value) > 0) {
//                    getCluster().expire(targetKey, expireSeconds);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return cacheOperate.execute(true, Boolean.class);
//    }
//
//    /**
//     * 判断是否安全写入
//     * @param key key
//     * @param expireMillSeconds expireMillSeconds
//     * @param value value
//     * @return true 成功 false 失败
//     */
//    public static boolean safeAddCacheByMillSeconds(String key, int expireMillSeconds, String value) {
//
//        if (StringUtils.isEmpty(key)) {
//            return false;
//        }
//
//        String targetKey = RedisCacheConstant.SAFE_PREFIX + key;
//
//        BaseCacheOperate cacheOperate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                if (getCluster().setnx(targetKey, value) > 0) {
//                    getCluster().pexpire(targetKey, expireMillSeconds);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return cacheOperate.execute(true, Boolean.class);
//
//    }
//
//
//    /**
//     * 增加指定的数字，并返回当前值
//     *
//     * @param cacheKey key
//     * @param incrNum 增加的数量
//     * @return 返回值
//     */
//    public static Long increaseBy(String cacheKey, long incrNum) {
//
//        BaseCacheOperate cacheOperate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().incrBy(cacheKey, incrNum);
//            }
//            @Override
//            public String operateAlias() {
//                return cacheKey;
//            }
//        };
//
//        return cacheOperate.execute(null, Long.class);
//
//    }
//
//    /**
//     * 自增操作，并返回当前的值。
//     *
//     * @param cacheKey key
//     * @return rs
//     */
//    public static Long increase(String cacheKey) {
//
//        BaseCacheOperate cacheOperate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().incr(cacheKey);
//            }
//            @Override
//            public String operateAlias() {
//                return cacheKey;
//            }
//        };
//
//        return cacheOperate.execute(null, Long.class);
//
//    }
//
//    public static Double zincrby(String key, double score, String member) {
//
//        BaseCacheOperate cacheOperate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zincrby(key, score, member);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//        return cacheOperate.execute(null, Double.class);
//    }
//
//    public static Double zincrby(String key, double score, String member, ZIncrByParams params) {
//
//        BaseCacheOperate cacheOperate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zincrby(key, score, member,params);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//        return cacheOperate.execute(null, Double.class);
//    }
//
//
//        /**
//         * 原子减操作，并返回当前的值。
//         * 操作失败返回null
//         * @param cacheKey key
//         * @return rs
//         */
//    public static Long decrease(String cacheKey) {
//
//        BaseCacheOperate cacheOperate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().decr(cacheKey);
//            }
//            @Override
//            public String operateAlias() {
//                return cacheKey;
//            }
//        };
//
//        return cacheOperate.execute(null, Long.class);
//    }
//
//    /**
//     * ttl 剩余生存时间 单位秒
//     * @param cacheKey key
//     * @return
//     *  -2  查询失败或缓存不存在
//     *  -1  key未设置过期时间
//     */
//    public static Long ttl(String cacheKey) {
//
//        BaseCacheOperate cacheOperate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().ttl(cacheKey);
//            }
//            @Override
//            public String operateAlias() {
//                return "ttl " + cacheKey;
//            }
//        };
//
//        //查询失败当做 不存在处理
//        return cacheOperate.execute(-2, Long.class);
//    }
//
//    /**
//     * 直接获取缓存内容
//     *
//     * @param key key
//     * @return value
//     */
//    public static String getCacheDirect(String key) {
//
//        if (StringUtils.isEmpty(key)) {
//            return null;
//        }
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().get(key);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, String.class);
//    }
//
//    /**
//     * 设置超时时间
//     *
//     * @param key key
//     * @param expireMinute 过期分钟
//     */
//    public static void setCacheExpire(String key, int expireMinute) {
//
//        if (StringUtils.isEmpty(key)) {
//            return;
//        }
//
//        if (expireMinute < 1) {
//            log.warn("expire minute < 1,return");
//            return;
//        }
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().expire(key, expireMinute * 60);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        operate.execute(null, null);
//    }
//
//    public static Long expire(String key, int seconds) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().expire(key, seconds);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Long.class);
//    }
//
//    /**
//     * This command works exactly like EXPIRE but the time to live of the key is specified in milliseconds instead of seconds.
//     * @param key value
//     * @param milliseconds 毫秒
//     * @return Integer reply, specifically:
//     * 1 if the timeout was set.
//     * 0 if key does not exist.
//     */
//    public static Long pexpire(String key, long milliseconds) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().pexpire(key, milliseconds);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Long.class);
//    }
//
//
//    /**
//     * 设置redids hash
//     * 多个field value不可为null 否则异常
//     */
//    public static String hmset(String key, Map<String, String> hash) {
//
//        if (StringUtils.isEmpty(key) || null == hash || hash.isEmpty()) {
//            return null;
//        }
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().hmset(key, hash);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, String.class);
//    }
//
//
//    public static Long hset(String key, String field, String value) {
//
//        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
//            return null;
//        }
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().hset(key, field, value);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Long.class);
//    }
//
//    public static Long hsetnx(String key, String field, String value) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().hsetnx(key, field, value);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Long.class);
//    }
//
//    /**
//     * <pre>
//     *   Set key to hold string value if key does not exist.
//     *   In that case, it is equal to SET.
//     *   When key already holds a value, no operation is performed.
//     *   SETNX is short for "SET if Not exists".
//     * </pre>
//     * @param key key
//     * @param value value
//     * @return
//     *  1 if the key was set;
//     *  0 if the key was not set
//     *  null set key error
//     */
//    public static Long setnx(String key, String value) {
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().setnx(key, value);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//        return operate.execute(null, Long.class);
//    }
//
//    /**
//     * 获取哈希中field是否存在
//     */
//    public static boolean isHashExits(String key, String field) {
//
//        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
//            return false;
//        }
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().hexists(key, field);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(false, Boolean.class);
//
//    }
//
//    /**
//     * 获取redis hash值
//     */
//    public static String hget(String key, String field) {
//
//        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
//            return null;
//        }
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().hget(key, field);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, String.class);
//    }
//
//    /**
//     * 获取redis hash多个值
//     */
//    public static List<String> hmget(String key, String... fields) {
//
//        if (StringUtils.isEmpty(key) || fields.length == 0) {
//            return null;
//        }
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().hmget(key, fields);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, List.class);
//    }
//
//    /**
//     * @param key key
//     * @param values values
//     * @return the length of the list after the push operations
//     */
//    public static Long lpush(String key, String... values) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().lpush(key, values);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(0L, Long.class);
//    }
//
//    public static Long rpush(String key, String... values) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().rpush(key, values);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(0L, Long.class);
//    }
//
//    /**
//     * Returns the specified elements of the list stored at key. The offsets start and stop are zero-based indexes, with 0 being the first element of the list (the head of the list), 1 being the next element and so on.
//     * These offsets can also be negative numbers indicating offsets starting at the end of the list. For example, -1 is the last element of the list, -2 the penultimate, and so on.
//     *
//     * @param key
//     * @param start
//     * @param end   Note that if you have a list of numbers from 0 to 100, LRANGE list 0 10 will return 11 elements, that is, the rightmost item is included
//     * @return list of elements in the specified range.
//     */
//    public static List<String> lrange(String key, long start, long end) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().lrange(key, start, end);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(new ArrayList<>(), List.class);
//    }
//
//    /**
//     * @param key key
//     * @param count count
//     * @param value value
//     * @return the number of removed elements.
//     */
//    public static Long lrem(String key, long count, String value) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().lrem(key, count, value);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(0L, Long.class);
//    }
//
//    public static String ltrim(String key, long start, long end) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().ltrim(key, start, end);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, String.class);
//    }
//
//    public static String lpop(String key) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().lpop(key);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, String.class);
//    }
//
//    public static Long zadd(String key, double score, String member) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zadd(key, score, member);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(0L, Long.class);
//    }
//
//    public static Long zadd(String key, Map<String, Double> scoreMembers) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zadd(key, scoreMembers);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Long.class);
//    }
//
//    public static Long zremrangeByRank(String key, long start, long end) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zremrangeByRank(key, start, end);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(0L, Long.class);
//    }
//
//    public static Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zrevrangeWithScores(key, start, end);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//        return operate.execute(new HashSet<>(), Set.class);
//    }
//
//    public static Set<String> zrevrange(String key, long start, long end) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zrevrange(key, start, end);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(new HashSet<>(), Set.class);
//    }
//
//    /**
//     * 返回有序集 key 中成员 member 的排名
//     * 其中有序集成员按 score 值递减(从大到小)排序。
//     * 排名以 0 为底，也就是说， score 值最大的成员排名为 0 。
//     *
//     * 使用 ZRANK key member 命令可以获得成员按 score 值递增(从小到大)排列的排名。
//     * @param key
//     * @param member
//     * @return
//     */
//    public static Long zrevrank(String key, String member) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zrevrank(key, member);
//            }
//            @Override
//            public String operateAlias() {
//                return "zrevrank " + key;
//            }
//        };
//
//        return operate.execute(null, Long.class);
//    }
//
//    /**
//     * 返回有序集 key 中成员 member 的排名。
//     * 其中有序集成员按 score 值递增(从小到大)顺序排列。
//     * 排名以 0 为底，也就是说， score 值最小的成员排名为 0 。
//     *
//     * 使用 ZREVRANK key member 命令可以获得成员按 score 值递减(从大到小)排列的排名。
//     * @param key
//     * @param member
//     * @return
//     */
//    public static Long zrank(String key, String member) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zrank(key, member);
//            }
//            @Override
//            public String operateAlias() {
//                return "zrank " + key;
//            }
//        };
//
//        return operate.execute(null, Long.class);
//    }
//
//    public static Set<Tuple> zrangeWithScores(String key, long start, long end) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zrangeWithScores(key, start, end);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(new HashSet<>(), Set.class);
//    }
//
//    public static Set<String> zrange(String key, long start, long end) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zrange(key, start, end);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(new HashSet<>(), Set.class);
//    }
//
//    public static String rename(String oldKey, String newKey) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().rename(oldKey, newKey);
//            }
//            @Override
//            public String operateAlias() {
//                return oldKey + "->" + newKey;
//            }
//        };
//
//        return operate.execute(null, String.class);
//    }
//
//    public static Long zunionstore(String destinationKey, String... fromKeys) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zunionstore(destinationKey, fromKeys);
//            }
//
//            @Override
//            public String operateAlias() {
//                return destinationKey;
//            }
//        };
//
//        return operate.execute(0L, Long.class);
//    }
//
//    public static Long sadd(String setKey, String... members) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().sadd(setKey, members);
//            }
//
//            @Override
//            public String operateAlias() {
//                return "sadd " + setKey;
//            }
//        };
//
//        return operate.execute(0L, Long.class);
//    }
//
//    public static Boolean sismember(String setKey, String member) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().sismember(setKey, member);
//            }
//
//            @Override
//            public String operateAlias() {
//                return "sadd " + setKey;
//            }
//        };
//
//        return operate.execute(null, Boolean.class);
//    }
//
//    public static String set(String key, String value, String nxxx, String expx, long time) {
//
//        BaseCacheOperate operate= new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().set(key, value, nxxx, expx, time);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, String.class);
//    }
//
//    public static String set(String key, String value) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().set(key, value);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, String.class);
//    }
//
//    public static Long hincrBy(String key, String field, long value) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().hincrBy(key, field, value);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Long.class);
//    }
//
//    public static Map<String, String> hgetAll(String key) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().hgetAll(key);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Map.class);
//    }
//
//    public static Long hdel(String key, String... fields) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().hdel(key, fields);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Long.class);
//    }
//
//    public static Long scard(String key) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().scard(key);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Long.class);
//    }
//
//    public static Set<String> smembers(String key) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().smembers(key);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Set.class);
//    }
//
//    public static String spop(String key) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().spop(key);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, String.class);
//    }
//
//    public static List<String> srandmember(String key, int count) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().srandmember(key, count);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, List.class);
//    }
//
//    public static Long zcount(String key, double min, double max) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zcount(key, min, max);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Long.class);
//    }
//
//    public static Long zcard(String key) {
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zcard(key);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//        return operate.execute(null, Long.class);
//    }
//
//    public static Long zcount(String key, String min, String max) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zcount(key, min, max);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Long.class);
//    }
//
//    public static Long zremrangeByScore(String key, double start, double end) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zremrangeByScore(key, start, end);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Long.class);
//    }
//
//    public static Long zrem(String key, String... member) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zrem(key, member);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Long.class);
//    }
//
//    public static Set<String> zrangeByScore(String key, double min, double max) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zrangeByScore(key, min, max);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Set.class);
//    }
//
//    public static Set<String> zrangeByScore(String key, String min, String max) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zrangeByScore(key, min, max);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Set.class);
//    }
//
//    public static Set<String> zrevrangeByScore(String key, double max, double min) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zrevrangeByScore(key, max, min);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Set.class);
//    }
//
//    public static Set<String> zrevrangeByScore(String key, String max, String min) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zrevrangeByScore(key, max, min);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Set.class);
//    }
//
//    public static Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zrevrangeByScore(key, max, min, offset, count);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Set.class);
//    }
//
//    public static Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zrangeByScoreWithScores(key, min, max);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Set.class);
//    }
//
//    public static Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zrevrangeByScoreWithScores(key, max, min);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Set.class);
//    }
//
//    public static Double zscore(String key,String member){
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().zscore(key, member);
//            }
//            @Override
//            public String operateAlias() {
//                return key;
//            }
//        };
//
//        return operate.execute(null, Double.class);
//    }
//
//    public static List<String> mget(String... keys) {
//        return mget(Arrays.asList(keys), String.class);
//    }
//
//    /**
//     * 批量查询缓存
//     * @param keyList 缓存key集合
//     * @param clazz 返回结果元素类型
//     * @param <T> 返回结果元素类型
//     * @return 缓存集合
//     */
//    public static <T> List<T> mget(List<String> keyList, Class<T> clazz) {
//        Map<JedisPool, List<String>> jedisPoolListMap = JedisClusterClient.groupKeyList(keyList);
//        List<String> result = new LinkedList<>();
//        for (Map.Entry<JedisPool, List<String>> entry : jedisPoolListMap.entrySet()) {
//            Jedis jedis = null;
//            Pipeline pipeline = null;
//            try {
//                jedis = entry.getKey().getResource();
//                pipeline = jedis.pipelined();
//                for (String key : entry.getValue()) {
//                    pipeline.get(key);
//                }
//                List<Object> list = pipeline.syncAndReturnAll();
//                for (Object item : list) {
//                    if (item != null) {
//                        result.add(item.toString());
//                    }
//                }
//            } catch (Exception e) {
//                log.error(e.getMessage(), e);
//            } finally {
//                IOUtils.close(pipeline);
//                IOUtils.close(jedis);
//            }
//        }
//        if (String.class.equals(clazz)) {
//            return (List<T>) result;
//        }
//        return result.stream().map(item -> JSON.parseObject(item, clazz)).collect(Collectors.toList());
//    }
//
//    /**
//     * 批量查询使用hash结构缓存
//     * @param keyList 缓存key集合
//     * @param clazz 返回结果元素类型
//     * @param <T> 返回结果元素类型
//     * @return 缓存集合
//     */
//    public static <T> List<T> mhget(List<String> keyList, Class<T> clazz) {
//        Map<JedisPool, List<String>> jedisPoolListMap = JedisClusterClient.groupKeyList(keyList);
//        List<Map<String, String>> result = new LinkedList<>();
//        for (Map.Entry<JedisPool, List<String>> entry : jedisPoolListMap.entrySet()) {
//            Jedis jedis = null;
//            Pipeline pipeline = null;
//            try {
//                jedis = entry.getKey().getResource();
//                pipeline = jedis.pipelined();
//                for (String key : entry.getValue()) {
//                    pipeline.hgetAll(key);
//                }
//                List<Object> list = pipeline.syncAndReturnAll();
//                for (Object item : list) {
//                    if (item != null && !CollectionUtils.isEmpty((Map<String, String>) item)) {
//                        result.add((Map<String, String>) item);
//                    }
//                }
//            } catch (Exception e) {
//                log.error(e.getMessage(), e);
//            } finally {
//                IOUtils.close(pipeline);
//                IOUtils.close(jedis);
//            }
//        }
//        if (String.class.equals(clazz)) {
//            return (List<T>) result.stream().map(JSON::toJSONString).collect(Collectors.toList());
//        }
//        return result.stream().map(item -> JSON.parseObject(JSON.toJSONString(item), clazz)).collect(Collectors.toList());
//    }
//
//    public static Long llen(String key) {
//
//        BaseCacheOperate operate = new BaseCacheOperate() {
//            @Override
//            public Object userOperate() {
//                return getCluster().llen(key);
//            }
//            @Override
//            public String operateAlias() {
//                return "llen:" + key;
//            }
//        };
//
//        return operate.execute(null, Long.class);
//    }
//
//}