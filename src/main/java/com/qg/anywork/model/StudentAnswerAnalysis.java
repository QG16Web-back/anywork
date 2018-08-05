package com.qg.anywork.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author logan
 * @date 2017/7/10
 */
@Data
@NoArgsConstructor
public class StudentAnswerAnalysis {

    /**
     * 分析id 暂无用处
     */
    private int studentAnswerAnalysisId;

    /**
     * 学生id
     */
    private int studentId;

    /**
     * 试题
     */
    private Question question;

    /**
     * 学生答案
     */
    private String studentAnswer;

    /**
     * 是否正确
     */
    private int isTrue;

    /**
     * 得分情况
     */
    private double socre;
}
