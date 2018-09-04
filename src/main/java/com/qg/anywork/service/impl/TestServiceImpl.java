package com.qg.anywork.service.impl;

import com.qg.anywork.dao.OrganizationDao;
import com.qg.anywork.dao.QuestionRedisDao;
import com.qg.anywork.dao.TestDao;
import com.qg.anywork.domain.UserRepository;
import com.qg.anywork.enums.StatEnum;
import com.qg.anywork.exception.organization.OrganizationException;
import com.qg.anywork.exception.test.TestException;
import com.qg.anywork.exception.testpaper.TestPaperException;
import com.qg.anywork.model.bo.StudentAnswer;
import com.qg.anywork.model.bo.StudentPaper;
import com.qg.anywork.model.bo.TeacherJudge;
import com.qg.anywork.model.bo.TeacherSubmit;
import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.model.po.*;
import com.qg.anywork.service.TestService;
import com.qg.anywork.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    private UserRepository userRepository;

    @Autowired
    private QuestionRedisDao questionRedisDao;

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

    @Override
    @SuppressWarnings("unchecked")
    public RequestResult listTest(int userId, int organizationId, int chapterId, int testPaperType) {
        List<TestPaper> testPapers = testDao.findByOrganizationIdAndChapterIdAndTestPaperType(organizationId, chapterId, testPaperType);
        if (testPapers.isEmpty()) {
            throw new TestPaperException(StatEnum.TEST_LIST_IS_NULL);
        }
        List<Map<String, Object>> data = new ArrayList<>();
        for (TestPaper testPaper : testPapers) {
            Map<String, Object> map = new HashMap<>();
            map.put("testpaperId", testPaper.getTestpaperId());
            map.put("testpaperTitle", testPaper.getTestpaperTitle());
            map.put("createTime", DateUtil.format(testPaper.getCreateTime()));
            map.put("endingTime", DateUtil.format(testPaper.getEndingTime()));
            map.put("testpaperScore", testPaper.getTestpaperScore());
            Date nowDate = DateUtil.getNowDate();
            int timeStatus;
            if (nowDate.after(testPaper.getEndingTime())) {
                // 过了时间，无法做题
                timeStatus = 1;
            } else if (nowDate.before(testPaper.getCreateTime())) {
                // 还未到做题时间
                timeStatus = 3;
            } else {
                timeStatus = 2;
            }
            map.put("timeStatus", timeStatus);
            // 判断是否
            StudentTestResult testResult = testDao.findTestResultByTestPaperIdAndUserIdAndOrganizationId(testPaper.getTestpaperId(),
                    userId, organizationId);
            if (testResult != null) {
                // 这烦人呢试卷已完成
                map.put("status", 1);
                map.put("score", testResult.getSocre());
                map.put("totalQuestions", 0);
                map.put("doneQuestions", 0);
            } else {
                // 查看redis是否有对应的缓存
                // redis key
                String key = "question_" + userId + "_" + testPaper.getTestpaperId();
                List<StudentAnswer> studentAnswers = (List<StudentAnswer>) questionRedisDao.getQuestionAnswer(key);
                if (studentAnswers == null) {
                    map.put("status", 3);
                    map.put("score", -1);
                    map.put("totalQuestions", 0);
                    map.put("doneQuestions", 0);
                } else {
                    map.put("status", 2);
                    map.put("score", -1);
                    map.put("totalQuestions", testDao.countQuestion(testPaper.getTestpaperId()));
                    map.put("doneQuestions", studentAnswers.size());
                }
            }
            data.add(map);
        }
        return new RequestResult<>(StatEnum.GET_TEST_SUCCESS, data);
    }

    @Override
    public RequestResult getDoneTestDetail(int userId, int testPaperId) {
        // 获取该试卷的结果
        StudentTestResult testResult = testDao.getTestResult(testPaperId, userId);
        Map<String, Object> map = new ConcurrentHashMap<>(4);
        map.put("studentId", userId);
        map.put("testpaperId", testPaperId);
        map.put("socre", testResult.getSocre());
        List<StudentAnswerAnalysis> studentAnswerAnalyses = testDao.getStudentAnswer(testPaperId, userId);
        map.put("studentAnswerAnalysis", studentAnswerAnalyses);
        return new RequestResult<>(StatEnum.GET_SUCCESS, map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public RequestResult getNoneTestDetail(int userId, int testPaperId, int choice) {
        List<Question> questions = testDao.getQuestionByTestpaperId(testPaperId);
        if (choice == 0 || choice == 2) {
            // 表示是要获取还未做的试卷
            // 获取想重新做之前做一半的题
            for (Question question : questions) {
                // 把答案置为空
                question.setKey("");
            }
        } else if (choice == 1) {
            // 继续做之前做一半的题
            // 从redis获取之前的答案
            String key = "question_" + userId + "_" + testPaperId;
            List<StudentAnswer> studentAnswers = (List<StudentAnswer>) questionRedisDao.getQuestionAnswer(key);
            for (Question question : questions) {
                boolean flag = false;
                for (StudentAnswer studentAnswer : studentAnswers) {
                    if (question.getQuestionId() == studentAnswer.getQuestionId()) {
                        question.setKey(studentAnswer.getStudentAnswer());
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    question.setKey("");
                }
            }
        } else {
            throw new TestException(StatEnum.ERROR_PARAM);
        }
        return new RequestResult<>(StatEnum.GET_TEST_SUCCESS, questions);
    }

    @Override
    @SuppressWarnings("unchecked")
    public RequestResult<StudentTestResult> submitTestPaper(StudentPaper studentPaper) throws ParseException {
        if (studentPaper.getTemporarySave() == 1) {
            // 临时保存
            String key = "question_" + studentPaper.getStudentId() + "_" + studentPaper.getTestpaperId();
            questionRedisDao.setQuestionAnswer(key, studentPaper.getStudentAnswer());
            return new RequestResult(StatEnum.SAVE_TEST_SUCCESS);
        } else if (studentPaper.getTemporarySave() == 0) {
            // 直接提交获得试卷分析
            // 删除该试卷对应的临时缓存
            String key = "question_" + studentPaper.getStudentId() + "_" + studentPaper.getTestpaperId();
            questionRedisDao.removeQuestionAnswer(key);

            // 获得对应试卷
            TestPaper testpaper = testDao.getTestPaperByTestpaperId(studentPaper.getTestpaperId());
            // 是否已经做过该套题的标志
            boolean flag = testDao.isSubmit(studentPaper.getTestpaperId(), studentPaper.getStudentId()) > 0;
            if (flag) {
                throw new TestException(StatEnum.EXAM_CANNOT_BE_SUBMITTED_REPEATEDLY);
            }
            if (DateUtil.parse(studentPaper.getEndTime()).after(testpaper.getEndingTime())) {
                // 超过了限定的时间
                throw new TestException(StatEnum.TEST_TIME_IS_OVER);
            }
            StudentTestResult studentTestResult = getTestResult(studentPaper);
            testDao.addTestResult(studentTestResult);
            CheckResult checkResult = new CheckResult();
            checkResult.setIfCheck(0);
            checkResult.setStudentId(studentPaper.getStudentId());
            checkResult.setTestpaper(testpaper);
            checkResult.setObject(studentTestResult.getSocre());
            checkResult.setSubject(0);
            testDao.addCheckResult(checkResult);
            for (StudentAnswerAnalysis studentAnswerAnalysis : studentTestResult.getStudentAnswerAnalysis()) {
                testDao.addStudentAnswer(studentAnswerAnalysis);
            }
            return new RequestResult<>(StatEnum.SUBMIT_TEST_SUCCESS, studentTestResult);
        }
        return new RequestResult<>(StatEnum.SUBMIT_TEST_FAIL);
    }

    @Override
    public RequestResult getQuestionDetail(int userId, int questionId) {
        StudentAnswerAnalysis studentAnswerAnalysis = testDao.getStudentAnswerAnalysis(userId, questionId);
        return new RequestResult<>(StatEnum.GET_SUCCESS, studentAnswerAnalysis);
    }

    private StudentTestResult getTestResult(StudentPaper studentPaper) {
        StudentTestResult studentTestResult = new StudentTestResult();
        studentTestResult.setTestpaperId(studentPaper.getTestpaperId());
        studentTestResult.setStudentId(studentPaper.getStudentId());
        List<StudentAnswerAnalysis> studentAnswerAnalyses = new ArrayList<>();
        double score = 0L;
        List<StudentAnswer> studentAnswers = studentPaper.getStudentAnswer();
        List<Question> questions = testDao.getQuestionByTestpaperId(studentPaper.getTestpaperId());
        for (Question question : questions) {
            int ifDo = 0;
            for (StudentAnswer studentAnswer : studentAnswers) {
                if (studentAnswer.getQuestionId() == question.getQuestionId()) {
                    ifDo = 1;
                    StudentAnswerAnalysis studentAnswerAnalysis = new StudentAnswerAnalysis();
                    studentAnswerAnalysis.setQuestion(question);
                    studentAnswerAnalysis.setStudentAnswer(studentAnswer.getStudentAnswer());
                    // 选择判断
                    if (question.getType() == 1 || question.getType() == 2) {
                        if (studentAnswer.getStudentAnswer().equals(question.getKey())) {
                            score += question.getSocre();
                            studentAnswerAnalysis.setIsTrue(1);
                            studentAnswerAnalysis.setSocre(question.getSocre());
                        } else {
                            studentAnswerAnalysis.setIsTrue(0);
                            studentAnswerAnalysis.setSocre(0);
                        }
                    } else if (question.getType() == 3) {
                        // 填空
                        int isTrue = 1;
                        String split = "∏";
                        int index;
                        int number = question.getOther();
                        double fillingScore = 0.0;
                        // 正确答案数组
                        String[] answer = question.getKey().split(split);
                        // 学生答案数组
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
                        score += fillingScore;
                    }
                    studentAnswerAnalysis.setStudentId(studentPaper.getStudentId());
                    studentAnswerAnalyses.add(studentAnswerAnalysis);
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
                studentAnswerAnalyses.add(studentAnswerAnalysis);
            }
        }
        studentTestResult.setStudentAnswerAnalysis(studentAnswerAnalyses);
        studentTestResult.setSocre(score);
        return studentTestResult;
    }
}
