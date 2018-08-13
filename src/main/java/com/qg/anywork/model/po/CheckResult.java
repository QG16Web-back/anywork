package com.qg.anywork.model.po;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ming
 */
@Data
@NoArgsConstructor
public class CheckResult {

    /**
     * 学生id
     */
    int studentId;

    /**
     * 学生姓名
     */
    String studentName;

    /**
     * 是否评卷
     */
    int ifCheck;

    /**
     * 客观题分数
     */
    double object;

    /**
     * 主观题分数
     */
    double subject;

    /**
     * 是否参与考试
     */
    int ifAttend;

    /**
     * 相应的试卷类
     */
    Testpaper testpaper;
}