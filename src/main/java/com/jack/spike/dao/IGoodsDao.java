package com.jack.spike.dao;

import com.jack.spike.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author Jack
 * @Date 2019/4/25 17:08
 */
@Mapper
public interface IGoodsDao {
    @Select("select g.*,sg.stock_count,sg.start_date,sg.end_date,sg.spike_price from spike_goods sg left join goods g on g.id = sg.goods_id")
    List<GoodsVo> getGoodsVoList();

    @Select("select g.*,sg.stock_count,sg.start_date,sg.end_date,sg.spike_price from spike_goods sg left join goods g on g.id = sg.goods_id where g.id=#{id} ")
    GoodsVo getGoodsVoByGoodsId(@Param("id") long id);

}
