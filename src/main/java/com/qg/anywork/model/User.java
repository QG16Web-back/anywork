package com.qg.anywork.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体
 *
 * @author FunriLy
 * @date 2017/7/10
 * From small beginnings comes great things.
 */
@Data
@NoArgsConstructor
public class User {

    /**
     * id
     */
    private int userId;

    /**
     * 昵称
     */
    private String userName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机
     */
    private String phone;

    /**
     * 标志，区分是学生还是教师, 0学生，1老师
     */
    private int mark;
}
