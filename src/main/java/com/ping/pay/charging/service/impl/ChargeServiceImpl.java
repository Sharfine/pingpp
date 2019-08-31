package com.ping.pay.charging.service.impl;

import com.ping.pay.charging.model.Goods;
import com.ping.pay.charging.model.OrderInfo;
import com.ping.pay.charging.service.IChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ChargeServiceImpl implements IChargeService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Goods getGoodsByGoodsId(Integer goodsId) {
        RowMapper<Goods> rowMapper = new BeanPropertyRowMapper<>(Goods.class);
        Goods goods = jdbcTemplate.queryForObject("select id as 'goodsId', name as 'goodsName', des as 'goodsDes', price as 'price' from goods where id = ?  and valid = 1",
                new Object[]{goodsId}, rowMapper);
        return goods;
    }

    @Override
    public void addOrderInfo(String userId, OrderInfo orderInfo) {
        jdbcTemplate.update("insert into order_info (id, user_id, goods_id, create_time, update_time, status) values (?,?,?,?,?,?)",
                new Object[]{orderInfo.getOrderId(), userId, orderInfo.getGoods().getGoodsId(), new Date(),  new Date(), 0});
    }
}
