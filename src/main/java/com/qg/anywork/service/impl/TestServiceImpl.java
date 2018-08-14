package com.qg.anywork.service.impl;

import com.qg.anywork.dao.OrganizationDao;
import com.qg.anywork.dao.TestDao;
import com.qg.anywork.dao.UserDao;
import com.qg.anywork.exception.organization.OrganizationException;
import com.qg.anywork.model.bo.*;
import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.enums.StatEnum;
import com.qg.anywork.exception.test.TestException;
import com.qg.anywork.model.po.*;
import com.qg.anywork.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create by ming on 18-8-5 下午9:53
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Slf4j
@Service
@Scope("prototype")
public class TestServiceImpl implements TestService {

    @Autowired
    private TestDao testDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private UserDao userDao;

    @Override
    public RequestResult<List<Testpaper>> getTestList(int organizationId, int userId) {
        if (organizationDao.getById(organizationId) == null) {
            throw new OrganizationException(StatEnum.INVALID_ORGANIZATION);
        }
        List<Testpaper> testpapers = testDao.getTestByOrganizationId(organizationId);
        List<Testpaper> studentpapers = testDao.getMyTest(userId);
        List<Testpaper> testpaperList = checkIfDo(testpapers, studentpapers);
        return new RequestResult<>(StatEnum.GET_TEST_SUCCESS, testpaperList);
    }


    @Override
    public RequestResult<List<Testpaper>> getPracticeList(int organizationId, int userId) {
        if (organizationDao.getById(organizationId) == null) {
            throw new OrganizationException(StatEnum.INVALID_ORGANIZATION);
        }
        List<Testpaper> practiceList = testDao.getPracticeByOrganizationId(organizationId);
        List<Testpaper> studentpapers = testDao.getMyPractice(userId);
        List<Testpaper> testpaperList = checkIfDo(practiceList, studentpapers);
        return new RequestResult<>(StatEnum.GET_TEST_SUCCESS, testpaperList);
    }

    /***
     * 检查是否有做
     * @param testpapers
     * @param studentpapers
     * @return
     */
    private List<Testpaper> checkIfDo(List<Testpaper> testpapers, List<Testpaper> studentpapers) {
        List<Testpaper> testpapersList = new ArrayList<>();
        for (Testpaper t : testpapers) {
            int flag = 0;
            //没做的flag
            for (Testpaper testpaper : studentpapers) {
                if (t.getTestpaperId() == testpaper.getTestpaperId()) {
                    t.setChapterId(0);
                    flag = 1;
                }
            }
            if (flag == 0) {
                t.setChapterId(-1);
            }
            testpapersList.add(t);
        }

        return testpapersList;
    }

    @Override
    public RequestResult<List<Testpaper>> getPracticeByOCId(int organizationId, int chapterId, int userId) {
        if (organizationDao.getById(organizationId) == null) {
            throw new OrganizationException(StatEnum.INVALID_ORGANIZATION);
        }
        List<Testpaper> practiceList = testDao.getPracticeByOCId(organizationId, chapterId);
        List<Testpaper> studentpapers = testDao.getMyPractice(userId);
        List<Testpaper> testpaperList = checkIfDo(practiceList, studentpapers);

        return new RequestResult<>(StatEnum.GET_TEST_SUCCESS, testpaperList);
    }

    @Override
    public RequestResult<List<Testpaper>> getMyPracticeList(int userId) {

        List<Testpaper> testpapers = testDao.getMyPractice(userId);
        for (Testpaper t : testpapers) {
            t.setChapterId(0);
        }
        return new RequestResult<>(StatEnum.GET_TEST_SUCCESS, testpapers);
    }

    @Override
    public RequestResult<List<CheckResult>> getPraceticeByOrganizationId(int userId, int organizationId) {

        List<CheckResult> practice = testDao.getUserPracticeByOrganizationId(userId, organizationId);

        List<CheckResult> checkResults = new ArrayList<>();
        List<Testpaper> testpapers = testDao.getPracticeByOrganizationId(organizationId);
        for (Testpaper t : testpapers) {
            int flag = 0;
            for (CheckResult c : practice) {
                if (t.getTestpaperId() == c.getTestpaper().getTestpaperId()) {
                    c.setIfAttend(1);
                    checkResults.add(c);
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                CheckResult checkResult = new CheckResult();
                checkResult.setIfAttend(0);
                checkResult.setObject(0);
                checkResult.setSubject(0);
                checkResult.setStudentId(userId);
                checkResult.setStudentName(userDao.selectById(userId).getUserName());
                checkResult.setIfCheck(0);
                checkResult.setTestpaper(t);
                checkResults.add(checkResult);
            }
        }
        return new RequestResult<>(1, "获取成功", checkResults);
    }

    @Override
    public RequestResult<List<Testpaper>> getMyTestList(int userId) {
        List<Testpaper> testpapers = testDao.getMyTest(userId);
        for (Testpaper t : testpapers) {
            t.setChapterId(0);
        }
        return new RequestResult<>(StatEnum.GET_TEST_SUCCESS, testpapers);
    }

    @Override
    public RequestResult<List<Question>> getQuestion(int testpaperId) {
        Testpaper testpaper = testDao.getTestPaperByTestpaperId(testpaperId);
        if (new Date().before(testpaper.getCreateTime()) && testpaper.getTestpaperType() == 1) {
            throw new TestException(StatEnum.EXAM_DID_NOT_START_YET);
        }
        List<Question> questions = testDao.getQuestionByTestpaperId(testpaperId);
        for (Question question : questions) {
            question.setKey(null);
        }
        return new RequestResult<>(StatEnum.GET_TEST_SUCCESS, questions);
    }

    /***
     * 获得考试结果
     * @param studentPaper  考试试卷
     * @return 试卷结果
     */
    @Override
    public StudentTestResult getResult(StudentPaper studentPaper) {

        StudentTestResult studentTestResult = new StudentTestResult();
        studentTestResult.setStudentId(studentPaper.getStudentId());
        studentTestResult.setTestpaperId(studentPaper.getTestpaperId());

        List<StudentAnswerAnalysis> studentAnswerAnalysises = new ArrayList<>();

        double socre = 0;

        //学生写的答案列表
        List<StudentAnswer> studentAnswers = studentPaper.getStudentAnswer();
        //试题集合
        List<Question> questions = testDao.getQuestionByTestpaperId(studentPaper.getTestpaperId());

        for (Question question : questions) {
            int ifDo = 0;
            for (StudentAnswer studentAnswer : studentAnswers) {
                if (studentAnswer.getQuestionId() == question.getQuestionId()) {
                    ifDo = 1;
                    StudentAnswerAnalysis studentAnswerAnalysis = new StudentAnswerAnalysis();
                    //分析题目类注入值
                    studentAnswerAnalysis.setQuestion(question);
                    studentAnswerAnalysis.setStudentAnswer(studentAnswer.getStudentAnswer());
                    //选择判断
                    if (question.getType() == 1 || question.getType() == 2) {
                        if (studentAnswer.getStudentAnswer().equals(question.getKey())) {
                            socre += question.getSocre();
                            studentAnswerAnalysis.setIsTrue(1);
                            studentAnswerAnalysis.setSocre(question.getSocre());
                        } else {
                            studentAnswerAnalysis.setIsTrue(0);
                            studentAnswerAnalysis.setSocre(0);
                        }
                    }
                    //填空
                    if (question.getType() == 3) {
                        int isTrue = 1;
                        String split = "∏";
                        int index;
                        int number = question.getOther();
                        double fillingScore = 0.0;
                        //正确答案数组
                        String[] answer = question.getKey().split(split);
                        //学生答案数组
                        String[] studentFillingAnswer = studentAnswer.getStudentAnswer().split(split);

                        for (index = 0; index < number; index++) {
                            if (answer[index].equals(studentFillingAnswer[index])) {
                                fillingScore += question.getSocre() / number;
                            } else {
                                isTrue = 0;
                            }
                        }
                        studentAnswerAnalysis.setIsTrue(isTrue == 1 ? 1 : 0);
                        studentAnswerAnalysis.setSocre(fillingScore);
                        socre += fillingScore;
                    }
                    studentAnswerAnalysis.setStudentId(studentPaper.getStudentId());
                    studentAnswerAnalysises.add(studentAnswerAnalysis);

                }

            }
            if (ifDo == 0) {
                //没写该道题
                StudentAnswerAnalysis studentAnswerAnalysis = new StudentAnswerAnalysis();
                //分析题目类注入值
                studentAnswerAnalysis.setQuestion(question);
                studentAnswerAnalysis.setStudentAnswer("");
                studentAnswerAnalysis.setIsTrue(0);
                studentAnswerAnalysis.setSocre(0);
                studentAnswerAnalysis.setStudentId(studentPaper.getStudentId());
                studentAnswerAnalysises.add(studentAnswerAnalysis);
            }
        }

        studentTestResult.setStudentAnswerAnalysis(studentAnswerAnalysises);
        studentTestResult.setSocre(socre);
        return studentTestResult;
    }

    @Override
    public RequestResult<StudentTestResult> submit(StudentPaper studentPaper) {

        // 获得对应试卷
        Testpaper testpaper = testDao.getTestPaperByTestpaperId(studentPaper.getTestpaperId());

        //是否已经做过该套题的标志
        boolean flag = testDao.isSubmit(testpaper.getTestpaperId(), studentPaper.getStudentId()) > 0;

        if (flag && testpaper.getTestpaperType() == 1) {
            throw new TestException(StatEnum.EXAM_CANNOT_BE_SUBMITTED_REPEATEDLY);
        }

        StudentTestResult studentTestResult = this.getResult(studentPaper);

        //如果为考试则必须校验考试时间
        if (testpaper.getTestpaperType() == 1) {
            if (new Date().after(testpaper.getEndingTime())) {
                throw new TestException(StatEnum.THE_EXAM_IS_OVER);
            }
        }

        if (flag) {
            testDao.updateTestResult(studentTestResult);
        } else {
            testDao.addTestResult(studentTestResult);
            if (testpaper.getTestpaperType() == 1) {
                CheckResult checkResult = new CheckResult();
                checkResult.setStudentId(studentPaper.getStudentId());
                checkResult.setIfCheck(0);
                checkResult.setTestpaper(testpaper);
                checkResult.setObject(studentTestResult.getSocre());
                checkResult.setSubject(0);
                testDao.addCheckResult(checkResult);
            }
        }
        List<StudentAnswerAnalysis> studentAnswerAnalysis = studentTestResult.getStudentAnswerAnalysis();
        if (!flag) {
            for (StudentAnswerAnalysis s : studentAnswerAnalysis) {
                testDao.addStudentAnswer(s);
            }
        } else {
            for (StudentAnswerAnalysis s : studentAnswerAnalysis) {
                testDao.updateStudentAnswer(s);
            }
        }
        return new RequestResult<>(StatEnum.SUBMIT_TEST_SUCCESS, studentTestResult);
    }

    @Override
    public void addTestpaper(Testpaper testpaper) {
        testDao.addTestpaper(testpaper);
    }

    @Override
    public boolean updateTextpaper(int testpaperId, int socre) {
        return testDao.updateSocreOfTestpaper(testpaperId, socre) == 1;
    }

    @Override
    public RequestResult<StudentTestResult> getDetail(int testpaperId, int userId) {
        StudentTestResult studentTestResult = testDao.getTestResult(testpaperId, userId);
        if (studentTestResult == null) {
            return new RequestResult<>(0, "没有此记录");
        }
        List<StudentAnswerAnalysis> studentAnswerAnalysis = testDao.getStudentAnswer(testpaperId, userId);
        studentTestResult.setStudentAnswerAnalysis(studentAnswerAnalysis);
        return new RequestResult<>(1, "成功", studentTestResult);
    }

    @Override
    public RequestResult<List<CheckResult>> getCheckResultByUser(int organizationId, int userId) {
        List<CheckResult> checkResultsByUser = testDao.getCheckResultByUser(organizationId, userId);
        List<CheckResult> checkResults = new ArrayList<CheckResult>();
        List<Testpaper> testpapers = testDao.getTestByOrganizationId(organizationId);
        for (Testpaper t : testpapers) {
            int flag = 0;
            for (CheckResult c : checkResultsByUser) {
                if (t.getTestpaperId() == c.getTestpaper().getTestpaperId()) {
                    c.setIfAttend(1);
                    checkResults.add(c);
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                CheckResult checkResult = new CheckResult();
                checkResult.setIfAttend(0);
                checkResult.setObject(0);
                checkResult.setSubject(0);
                checkResult.setStudentId(userId);
                checkResult.setStudentName(userDao.selectById(userId).getUserName());
                checkResult.setIfCheck(0);
                checkResult.setTestpaper(t);
                checkResults.add(checkResult);
            }
        }

        return new RequestResult<>(1, "获取成功", checkResults);
    }

    @Override
    public RequestResult<List<CheckResult>> getCheckResultByTestpaperId(int organizationId, int testpaperId) {
        List<CheckResult> checkResultsByTest = testDao.getCheckResultByTestpaperId(organizationId, testpaperId);
        List<CheckResult> checkResults = new ArrayList<CheckResult>();
        List<User> users = organizationDao.getOrganizationPeople(organizationId);
        for (User u : users) {
            int flag = 0;
            for (CheckResult c : checkResultsByTest) {
                if (c.getStudentId() == u.getUserId()) {
                    c.setIfAttend(1);
                    checkResults.add(c);
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                CheckResult checkResult = new CheckResult();
                checkResult.setIfAttend(0);
                checkResult.setObject(0);
                checkResult.setSubject(0);
                checkResult.setStudentId(u.getUserId());
                checkResult.setStudentName(u.getUserName());
                checkResult.setIfCheck(0);
                checkResult.setTestpaper(testDao.getTestPaperByTestpaperId(testpaperId));
                checkResults.add(checkResult);
            }
        }
        return new RequestResult<>(1, "获取成功", checkResults);
    }

    @Override
    public void updateStudentTest(TeacherSubmit teacherSubmit) {
        List<TeacherJudge> teacherJudges = teacherSubmit.getTeacherJudge();
        double subject = 0;
        double object = 0;
        for (TeacherJudge t : teacherJudges) {
            int type = testDao.getQuestionById(t.getQuestionId()).getType();
            if (type == 4 || type == 6 || type == 5) {
                subject += t.getSocre();
            } else {
                object += t.getSocre();
            }
            //更新studentAnswer
            testDao.updateStudentAnswerSocre(t.getSocre(), teacherSubmit.getStudentId(), t.getQuestionId());
        }
        //更新checkResult
        testDao.updateCheckResult(subject, object, teacherSubmit.getTestpaperId(), teacherSubmit.getStudentId());

        //更新testResult
        StudentTestResult s = new StudentTestResult();
        s.setSocre(object);
        s.setStudentId(teacherSubmit.getStudentId());
        s.setTestpaperId(teacherSubmit.getTestpaperId());
        testDao.updateTestResult(s);

        StudentTestResult studentTestResult = testDao.getTestResult(teacherSubmit.getTestpaperId(), teacherSubmit.getStudentId());
        studentTestResult.setSocre(studentTestResult.getSocre() + subject);
        testDao.updateTestResult(studentTestResult);
    }
}
