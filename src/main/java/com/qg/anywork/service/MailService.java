package com.qg.anywork.service;

import com.qg.anywork.dto.RequestResult;
import com.qg.anywork.model.User;


/**
 * @author FunriLy
 * @date 2017/8/20
 * From small beginnings comes great things.
 */
public interface MailService {

    /**
     * 发送密码邮箱
     *
     * @param email email
     * @return request result
     */
    RequestResult<?> sendPasswordMail(String email);

    /**
     * 发送注册邮箱验证
     *
     * @param user 用户
     * @return request result
     */
    RequestResult<Integer> sendRegisterMail(User user);

    /**
     * 忘记密码邮箱验证
     *
     * @param email email
     * @return 密码
     */
    String resetPassword(String email);
}
