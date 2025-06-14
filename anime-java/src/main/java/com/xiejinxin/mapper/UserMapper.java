package com.xiejinxin.mapper;

import com.xiejinxin.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user")
    public List<User> selectAll();
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);
    @Insert("INSERT INTO user (username, password) VALUES (#{username}, #{password})")
    void insert(User user);
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Integer id);
    @Update("UPDATE user SET password = #{password} WHERE id = #{userId}")
    void updatePassword(@Param("userId") Integer userId, @Param("password") String password);
}
