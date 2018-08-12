package com.qg.anywork.model.bo;

import com.qg.anywork.model.bo.TeacherJudge;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author logan
 * @date 2017/8/1
 */
@Data
@NoArgsConstructor
public class TeacherSubmit {

    /**
     * 答题者Id
     */
    private int studentId;

    /**
     * 试卷id
     */
    private int testpaperId;

    /**
     * 老师评分
     */
    private List<TeacherJudge> teacherJudge;
}
