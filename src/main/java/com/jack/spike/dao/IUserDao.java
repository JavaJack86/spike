package com.jack.spike.dao;

import com.jack.spike.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author Jack
 * @Date 2019/4/24 16:20
 */
@Mapper
public interface IUserDao {
    @Select("select * from user where id = #{id}")
    User getUserById(@Param("id") long id);
}
