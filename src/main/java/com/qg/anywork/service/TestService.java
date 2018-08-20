package com.qg.anywork.service;

import com.qg.anywork.model.bo.StudentPaper;
import com.qg.anywork.model.bo.StudentTestResult;
import com.qg.anywork.model.bo.TeacherSubmit;
import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.model.po.CheckResult;
import com.qg.anywork.model.po.Question;
import com.qg.anywork.model.po.TestPaper;

import java.util.List;

/**
 * @author logan
 * @date 2017/7/10
 */
public interface TestService {

    /***
     * 获取试题卷集合
     * @param organizationId　组织ID
     * @param userId          用户ID
     * @return 试卷集合
     */
    RequestResult<List<TestPaper>> getTestList(int organizationId, int userId);


    /***
     * 获取练习卷集合
     * @param organizationId 组织ID
     * @param userId         用户ID
     * @return 练习集合
     */
    RequestResult<List<TestPaper>> getPracticeList(int organizationId, int userId);

    /***
     * 根据章节id和组织id获取练习题
     * @param organizationId 组织ID
     * @param chapterId      章节ID
     * @param userId         用户ID
     * @return 练习题
     */
    RequestResult<List<TestPaper>> getPracticeByOCId(int organizationId, int chapterId, int userId);

    /***
     * 获取我做过的练习卷集合
     * @param userId 用户ID
     * @return 练习卷集合
     */
    RequestResult<List<TestPaper>> getMyPracticeList(int userId);

    /***
     * 获取某学生组织下做过的练习卷集合
     * @param userId 用户ID
     * @param organizationId 组织ID
     * @return 试卷列表
     */
    RequestResult<List<CheckResult>> getPraceticeByOrganizationId(int userId, int organizationId);

    /***
     * 获取我做过的考试卷集合
     * @param userId 用户ID
     * @return 考试卷集合
     */
    RequestResult<List<TestPaper>> getMyTestList(int userId);

    /***
     * 获取问题集合
     * @param testpaperId 试卷ID
     * @return Question
     */
    RequestResult<List<Question>> getQuestion(int testpaperId);

    /***
     * 获得考试结果
     * @param studentPaper  考试试卷
     * @return 考试结果
     */
    StudentTestResult getResult(StudentPaper studentPaper);

    /***
     * 提交试卷获取结果
     * @param studentPaper 学生答卷
     * @return 测试结果
     */
    RequestResult<StudentTestResult> submit(StudentPaper studentPaper);

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

}
