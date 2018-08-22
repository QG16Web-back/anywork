package com.qg.anywork.service;

import com.qg.anywork.model.bo.StudentPaper;
import com.qg.anywork.model.bo.TeacherSubmit;
import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.model.po.CheckResult;
import com.qg.anywork.model.po.Question;
import com.qg.anywork.model.po.StudentTestResult;
import com.qg.anywork.model.po.TestPaper;

import java.text.ParseException;
import java.util.List;

/**
 * @author logan
 * @date 2017/7/10
 */
public interface TestService {

    /***
     * 根据章节id和组织id获取练习题
     * @param organizationId 组织ID
     * @param chapterId      章节ID
     * @param userId         用户ID
     * @return 练习题
     */
    RequestResult<List<TestPaper>> getPracticeByOCId(int organizationId, int chapterId, int userId);

    /***
     * 获取某学生组织下做过的练习卷集合
     * @param userId 用户ID
     * @param organizationId 组织ID
     * @return 试卷列表
     */
    RequestResult<List<CheckResult>> getPraceticeByOrganizationId(int userId, int organizationId);

    /***
     * 获取问题集合
     * @param testpaperId 试卷ID
     * @return Question
     */
    RequestResult<List<Question>> getQuestion(int testpaperId);

    /**
     * 增加一张试卷/练习
     *
     * @param testpaper 试卷
     */
    void addTestpaper(TestPaper testpaper);

    //更新一张试卷/练习的总分
    boolean updateTextpaper(int testpaperId, int socre);

    /***
     * 获取学生试卷详情
     * @param testpaperId 试卷ID
     * @param userId 用户ID
     * @return 试卷详情
     */
    RequestResult<StudentTestResult> getDetail(int testpaperId, int userId);

    /***
     * 获取组织下某学生的考试列表
     * @param organizationId
     * @param userId
     * @return
     */
    RequestResult<List<CheckResult>> getCheckResultByUser(int organizationId, int userId);

    /***
     *获取某套题组织内成员的完成情况
     * @param organizationId
     * @param testpaperId
     * @return
     */
    RequestResult<List<CheckResult>> getCheckResultByTestpaperId(int organizationId, int testpaperId);

    /***
     * 更新相关分数
     * @param teacherSubmit teacherSubmit
     */
    void updateStudentTest(TeacherSubmit teacherSubmit);


    /**
     * 获取某个章节特定的题目
     *
     * @param userId         用户ID
     * @param organizationId 组织ID
     * @param chapterId      章节ID
     * @param testPaperType  试卷类型
     * @return 试卷列表
     */
    RequestResult listTest(int userId, int organizationId, int chapterId, int testPaperType);

    /**
     * 获取已做过试卷的详情
     *
     * @param userId      用户ID
     * @param testPaperId 试卷ID
     * @return 试卷详情
     */
    RequestResult getDoneTestDetail(int userId, int testPaperId);

    /**
     * 获取详细的试题（完成一部分和还未做）
     *
     * @param userId      用户ID
     * @param testPaperId 试卷ID
     * @param choice      对做一半的试卷的选择 1代表继续做，2代表重新做，如果是对于还未做的试卷，值为0
     * @return 详细试题
     */
    RequestResult getNoneTestDetail(int userId, int testPaperId, int choice);

    /**
     * 试卷提交
     *
     * @param studentPaper 学生提交的答案
     * @return 提交结果
     * @throws ParseException parseException
     */
    RequestResult<StudentTestResult> submitTestPaper(StudentPaper studentPaper) throws ParseException;

    /**
     * 获取学生这道题的详细信息
     *
     * @param userId     用户D
     * @param questionId 题目ID
     * @return 详细信息
     */
    RequestResult getQuestionDetail(int userId, int questionId);

    /**
     * 按章节获取我的错题
     *
     * @param userId    学生ID
     * @param chapterId 章节ID
     * @return 错题列表
     */
    RequestResult getWrongQuestionList(int userId, int chapterId);
}
