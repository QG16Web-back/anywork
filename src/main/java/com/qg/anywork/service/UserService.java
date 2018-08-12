package com.qg.anywork.service;

import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.model.po.User;
import org.springframework.stereotype.Service;

/**
 * @author FunriLy
 * @date 2017/7/10
 * From small beginnings comes great things.
 */
@Service
public interface UserService {

    /**
     * 校验用户信息
     *
     * @param user 用户
     */
    void userMessageCheck(User user);

    /**
     * 用户注册
     *
     * @param email 邮箱
     */
    void register(String email);

    /**
     * 用户登录
     *
     * @param studentId 学号
     * @param password  密码
     * @return request result
     */
    RequestResult<User> login(String studentId, String password);

    /**
     * 更新用户信息
     *
     * @param user user
     * @return 用户信息
     */
    RequestResult<User> updateUser(User user);

    /**
     * 获得用户个人信息
     *
     * @param userId 用户ID
     * @return user
     */
    RequestResult<User> findUserInfo(int userId);

    /**
     * 更新密码
     *
     * @param userId      用户ID
     * @param newPassword 新密码
     * @param oldPassword 旧密码
     * @return 修改成功返回true，修改失败返回false
     */
    boolean modifyPassword(int userId, String oldPassword, String newPassword);

    /**
     * 添加学生
     *
     * @return Request result
     */
    RequestResult addStudent();
}
