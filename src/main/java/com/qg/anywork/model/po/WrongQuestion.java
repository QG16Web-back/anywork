package com.qg.anywork.model.po;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create by ming on 18-8-22 下午10:56
 * 错题实体类
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Data
@Builder
@NoArgsConstructor
public class WrongQuestion {
    /**
     * 错题ID
     */
    private Integer wrongQuestionId;

    /**
     * 章节ID
     */
    private Integer chapterId;

    /**
     * 问题
     */
    private Question question;

    /**
     * 学生ID
     */
    private Integer studentId;

    /**
     * 学生答案
     */
    private String studentAnswer;

    private int isTrue;
}
