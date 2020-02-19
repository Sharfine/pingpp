package com.fun.daily.charging.service.impl;

import com.alibaba.fastjson.JSON;
import com.fun.daily.charging.dao.ChargeMapper;
import com.fun.daily.charging.model.Goods;
import com.fun.daily.charging.model.OrderInfo;
import com.fun.daily.charging.service.IChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 计费的service
 */
@Service
public class ChargeServiceImpl implements IChargeService {

    private static final String GOODS_REDIS_KEY = "goods_%s";

    private static final String ORDER_STATUS_REDIS_KEY = "order_%s";

    @Autowired
    private ChargeMapper chargeDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Goods getGoodsByGoodsId(Integer goodsId) {
        Goods goods = null;
        String key = String.format(GOODS_REDIS_KEY, goodsId);
        if(stringRedisTemplate.hasKey(key)){
            goods = JSON.parseObject(stringRedisTemplate.opsForValue().get(key), Goods.class);
        }else{
            goods = chargeDao.getGoodsByGoodsId(goodsId);
            stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(goods),6, TimeUnit.HOURS);
        }
        return goods;
    }

    @Override
    public void addOrderInfo(OrderInfo orderInfo) {
        chargeDao.addOrderInfo(orderInfo);
    }

    @Override
    public OrderInfo queryOrderInfo(String orderId) {
        OrderInfo orderInfo = null;
        String key = String.format(ORDER_STATUS_REDIS_KEY, orderId);
        if(stringRedisTemplate.hasKey(key)){
            orderInfo = JSON.parseObject(stringRedisTemplate.opsForValue().get(key), OrderInfo.class);
        }else{
            orderInfo = chargeDao.queryOrderInfo(orderId);
            stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(orderInfo),6, TimeUnit.HOURS);
        }
        return orderInfo;
    }

    @Override
    public void updateOrderStatus(String orderId, Integer status) {
        chargeDao.updateOrderStatus(orderId, status);
        stringRedisTemplate.delete(String.format(ORDER_STATUS_REDIS_KEY, orderId));
    }

}
