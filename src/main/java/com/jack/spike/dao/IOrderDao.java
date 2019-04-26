package com.jack.spike.dao;

import com.jack.spike.model.OrderInfo;
import com.jack.spike.model.SpikeOrder;
import org.apache.ibatis.annotations.*;

/**
 * @Author Jack
 * @Date 2019/4/26 9:54
 */
@Mapper
public interface IOrderDao {
    @Select("select id from orderInfo where user_id = #{userId} and goods_id = #{goodsId}")
    OrderInfo getOrderInfoByUserIdAndGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("insert into orderInfo(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    long insertOrderInfo(OrderInfo orderInfo);

    @Insert("insert into spike_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    int insertSpikeOrderInfo(SpikeOrder spikeOrder);
}
