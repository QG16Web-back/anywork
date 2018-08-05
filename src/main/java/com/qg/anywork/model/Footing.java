package com.qg.anywork.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 试卷与学生的关系表
 *
 * @author FunriLy
 * @date 2017/7/10
 * From small beginnings comes great things.
 */
@Data
@NoArgsConstructor
public class Footing {

    /**
     * 关系ID
     */
    private int footingId;

    /**
     * 试卷ID
     */
    private int textpaperId;

    /**
     * 时间ID
     */
    private int studentId;

    /**
     * 预留字段
     */
    private int other;
}
