package com.qg.anywork.model.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 组织实体
 *
 * @author FunriLy
 * @date 2017/7/10
 * From small beginnings comes great things.
 */
@Data
@NoArgsConstructor
public class Organization implements Serializable {

    /**
     * ID
     */
    private int organizationId;

    /**
     * 教师ID
     */
    private int teacherId;

    /**
     * 教师名称
     */
    private String teacherName;

    /**
     * 组织名
     */
    private String organizationName;

    /**
     * 描述
     */
    private String description;

    /**
     * 口令
     */
    private long token;

    /**
     * 组织人数
     */
    private int count;

    /**
     * 判断字段，标志学生是否是该组织成员
     */
    private int isJoin;

    //TODO:考虑老师登录后是否要带上学生列表或者试卷列表
}
