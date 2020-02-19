//package com.ping.daily.common.redis;
//
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.reflect.FieldUtils;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
//import org.springframework.stereotype.Component;
//import redis.clients.jedis.*;
//import redis.clients.util.JedisClusterCRC16;
//
//import java.lang.reflect.Field;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
///**
// * 服务端集群的Redis使用方式，其内部已经实现了池的管理，因此外部直接获取使用。
// *
// * @author cqchen 2016年3月20日23:08:44
// * @author yugj
// */
//@Component
//@Slf4j
//public class JedisClusterClient implements InitializingBean {
//
//    @Autowired
//    private RedisProperties redisProperties;
//
//    private JedisCluster jedisCluster = null;
//
//    /**
//     * 集群信息
//     */
//    private JedisClusterInfoCache jedisClusterInfoCache = null;
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxIdle(redisProperties.getJedis().getPool().getMaxIdle());
//        poolConfig.setMaxTotal(redisProperties.getJedis().getPool().getMaxActive());
//        poolConfig.setMaxWaitMillis(redisProperties.getJedis().getPool().getMaxWait().toMillis());
//        poolConfig.setTestOnBorrow(true);
//        poolConfig.setTestOnReturn(true);
//
//        //接单信息
//        Set<HostAndPort> nodes = new HashSet<>();
//
//        if(redisProperties.getCluster() != null){
//            List<String> hosts = redisProperties.getCluster().getNodes();
//            for (String host : hosts) {
//                nodes.add(new HostAndPort(host.split(":")[0], Integer.parseInt(host.split(":")[1])));
//            }
//        }else{
//            nodes.add(new HostAndPort(redisProperties.getHost(), redisProperties.getPort()));
//        }
//
//        //2.9.0 jedis新支持
//        this.jedisCluster = new JedisCluster(nodes, 2000, 2000, 20, redisProperties.getPassword(), poolConfig);
//        initJedisClusterInfoCache();
//
//        RedisPoolHolder.instance = this;
//
//        log.info("redis instance init success");
//    }
//
//    /**
//     * 初始化jedisCluster信息
//     */
//    private void initJedisClusterInfoCache() {
//        try {
//            Field handlerField = FieldUtils.getDeclaredField(BinaryJedisCluster.class, "connectionHandler", true);
//            JedisClusterConnectionHandler handler = (JedisClusterConnectionHandler) handlerField.get(jedisCluster);
//            Field cacheField = FieldUtils.getDeclaredField(JedisClusterConnectionHandler.class, "cache", true);
//            jedisClusterInfoCache = (JedisClusterInfoCache) cacheField.get(handler);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//    /**
//     * 缓存key按照集群node分组
//     * cluster环境会把数据按key哈希到16384个slot上，每个redis node负责一部分的slot
//     * 只支持对同一node上的数据进行批量操作,所以要把n个key按照node分组
//     * @param keyList key集合
//     * @return 分组结果
//     */
//    public static Map<JedisPool, List<String>> groupKeyList(List<String> keyList) {
//        Map<JedisPool, List<String>> jedisPoolListMap = Maps.newHashMap();
//        for (String key : keyList) {
//            JedisPool jedisPool = RedisPoolHolder.instance.jedisClusterInfoCache.getSlotPool(JedisClusterCRC16.getSlot(key));
//            if (jedisPoolListMap.containsKey(jedisPool)) {
//                jedisPoolListMap.get(jedisPool).add(key);
//            } else {
//                jedisPoolListMap.put(jedisPool, Lists.newArrayList(key));
//            }
//        }
//        return jedisPoolListMap;
//    }
//
//    /**
//     * 获取Jedis实例
//     * @return redis 实例
//     */
//    static JedisCluster getResource() {
//        return RedisPoolHolder.instance.jedisCluster;
//    }
//
//    static class RedisPoolHolder {
//        static JedisClusterClient instance;
//    }
//
//}