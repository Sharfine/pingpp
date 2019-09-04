package com.ping.pay.charging.service.impl;

import com.ping.pay.charging.dao.ChargeMapper;
import com.ping.pay.charging.model.Goods;
import com.ping.pay.charging.model.OrderInfo;
import com.ping.pay.charging.service.IChargeService;
import com.ping.pay.common.redis.annotation.NeedCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChargeServiceImpl implements IChargeService {

    @Autowired
    private ChargeMapper chargeDao;

    @Override
    @NeedCache(rangeFrom = 23 * 60, rangeTo = 24 * 60)
    public Goods getGoodsByGoodsId(Integer goodsId) {
        return chargeDao.getGoodsByGoodsId(goodsId);
    }

    @Override
    public void addOrderInfo(String userId, OrderInfo orderInfo) {
        chargeDao.addOrderInfo(orderInfo.getOrderId(), userId, orderInfo.getGoods().getGoodsId());
    }
}
