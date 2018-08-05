package com.qg.anywork.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author logan
 * @date 2017/7/10
 */
@Data
@NoArgsConstructor
public class StudentPaper {

    /**
     * 学生答卷id
     */
    private int studentPaperId;

    /**
     * 学生做的答案
     */
    private List<StudentAnswer> studentAnswer;

    /**
     * 答题者名字
     */
    private String userName;

    /**
     * 答题者Id
     */
    private int studentId;

    /**
     * 试卷id
     */
    private int testpaperId;

    /**
     * 开始答题的时间
     */
    private Date startTime;

    /**
     * 答题结束的时间
     */
    private Date endTime;
}
