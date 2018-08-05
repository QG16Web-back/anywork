package com.qg.anywork.dao;

import com.qg.anywork.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author FunriLy
 * @date 2017/7/10
 * From small beginnings comes great things.
 */
@Mapper
@Repository
public interface UserDao {

    /**
     * 插入用户
     *
     * @param user 用户
     */
    void insertUser(@Param("user") User user);

    /**
     * 更新用户消息
     *
     * @param user 用户
     * @return int
     */
    int updateUser(@Param("user") User user);

    /**
     * 根据用户id来获得用户消息
     *
     * @param userId 用户ID
     * @return user
     */
    User selectById(@Param("userId") int userId);

    /**
     * 根据邮箱查找用户
     *
     * @param email email
     * @return User
     */
    User selectByEmail(@Param("email") String email);

    /**
     * 根据用户id列表查找用户
     *
     * @param userIdList
     * @return
     */
    List<User> selectUserByIdList(List<Integer> userIdList);
}
