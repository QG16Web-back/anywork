package com.qg.anywork.service.impl;

import com.qg.anywork.dao.OrganizationDao;
import com.qg.anywork.dao.TestDao;
import com.qg.anywork.dao.UserDao;
import com.qg.anywork.domain.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Override
    public RequestResult<List<TestPaper>> getTestList(int organizationId, int userId) {
        if (organizationDao.getById(organizationId) == null) {
            throw new OrganizationException(StatEnum.INVALID_ORGANIZATION);
        }
        List<TestPaper> testPapers = testDao.getTestByOrganizationId(organizationId);
        List<TestPaper> studentpapers = testDao.getMyTest(userId);
        List<TestPaper> testPaperList = checkIfDo(testPapers, studentpapers);
        return new RequestResult<>(StatEnum.GET_TEST_SUCCESS, testPaperList);
    }


    @Override
    public RequestResult<List<TestPaper>> getPracticeList(int organizationId, int userId) {
        if (organizationDao.getById(organizationId) == null) {
            throw new OrganizationException(StatEnum.INVALID_ORGANIZATION);
        }
        List<TestPaper> practiceList = testDao.getPracticeByOrganizationId(organizationId);
        List<TestPaper> studentpapers = testDao.getMyPractice(userId);
        List<TestPaper> testPaperList = checkIfDo(practiceList, studentpapers);
        return new RequestResult<>(StatEnum.GET_TEST_SUCCESS, testPaperList);
    }

    /***
     * 检查是否有做
     * @param testPapers
     * @param studentpapers
     * @return
     */
    private List<TestPaper> checkIfDo(List<TestPaper> testPapers, List<TestPaper> studentpapers) {
        List<TestPaper> testpapersList = new ArrayList<>();
        for (TestPaper t : testPapers) {
            int flag = 0;
            //没做的flag
            for (TestPaper testpaper : studentpapers) {
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
    public RequestResult<List<TestPaper>> getPracticeByOCId(int organizationId, int chapterId, int userId) {
        if (organizationDao.getById(organizationId) == null) {
            throw new OrganizationException(StatEnum.INVALID_ORGANIZATION);
        }
        List<TestPaper> practiceList = testDao.getPracticeByOCId(organizationId, chapterId);
        List<TestPaper> studentpapers = testDao.getMyPractice(userId);
        List<TestPaper> testPaperList = checkIfDo(practiceList, studentpapers);

        return new RequestResult<>(StatEnum.GET_TEST_SUCCESS, testPaperList);
    }

    @Override
    public RequestResult<List<TestPaper>> getMyPracticeList(int userId) {

        List<TestPaper> testPapers = testDao.getMyPractice(userId);
        for (TestPaper t : testPapers) {
            t.setChapterId(0);
        }
        return new RequestResult<>(StatEnum.GET_TEST_SUCCESS, testPapers);
    }

    @Override
    public RequestResult<List<CheckResult>> getPraceticeByOrganizationId(int userId, int organizationId) {

        List<CheckResult> practice = testDao.getUserPracticeByOrganizationId(userId, organizationId);

        List<CheckResult> checkResults = new ArrayList<>();
        List<TestPaper> testPapers = testDao.getPracticeByOrganizationId(organizationId);
        for (TestPaper t : testPapers) {
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
                checkResult.setStudentName(userRepository.findByUserId(userId).getUserName());
                checkResult.setIfCheck(0);
                checkResult.setTestpaper(t);
                checkResults.add(checkResult);
            }
        }
        return new RequestResult<>(1, "获取成功", checkResults);
    }

    @Override
    public RequestResult<List<TestPaper>> getMyTestList(int userId) {
        List<TestPaper> testPapers = testDao.getMyTest(userId);
        for (TestPaper t : testPapers) {
            t.setChapterId(0);
        }
        return new RequestResult<>(StatEnum.GET_TEST_SUCCESS, testPapers);
    }

    @Override
    public RequestResult<List<Question>> getQuestion(int testpaperId) {
        TestPaper testpaper = testDao.getTestPaperByTestpaperId(testpaperId);
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
        TestPaper testpaper = testDao.getTestPaperByTestpaperId(studentPaper.getTestpaperId());

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
    public void addTestpaper(TestPaper testpaper) {
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
        List<TestPaper> testPapers = testDao.getTestByOrganizationId(organizationId);
        for (TestPaper t : testPapers) {
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
                checkResult.setStudentName(userRepository.findByUserId(userId).getUserName());
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
