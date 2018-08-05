package com.qg.anywork.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ming
 */
@Data
@NoArgsConstructor
public class TeacherJudge {

    /**
     * 问题id
     */
    private int questionId;

    /**
     * 老师所给分数
     */
    private double socre;
}
