package com.qg.anywork.web;

import com.qg.anywork.enums.StatEnum;
import com.qg.anywork.model.bo.StudentPaper;
import com.qg.anywork.model.bo.StudentTestResult;
import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.model.po.*;
import com.qg.anywork.service.ChapterService;
import com.qg.anywork.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author logan
 * @date 2017/7/12
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private ChapterService chapterService;

    @Resource(name = "defaultThreadPool")
    private ThreadPoolTaskExecutor executor;

    /***
     * 获取试题集合
     * @param map map
     *            organizationId 组织ID
     * @return 试题列表
     */
    @RequestMapping(value = "/testList", method = RequestMethod.POST)
    public RequestResult<List<Testpaper>> search(@RequestBody Map map, HttpServletRequest request) {
        String organizationId = (String) map.get("organizationId");
        if (organizationId == null || "".equals(organizationId)) {
            return new RequestResult<>(StatEnum.REQUEST_ERROR);
        }
        User user = (User) request.getSession().getAttribute("user");
        return testService.getTestList(Integer.parseInt(organizationId), user.getUserId());
    }


    /***
     * 根据组织id和章节id获取练习集合
     * @param map map
     *            organizationId 组织ID
     *            chapterId      章节ID
     * @return 试卷列表
     */
    @RequestMapping(value = "/practiceListByChapter", method = RequestMethod.POST)
    public RequestResult<List<Testpaper>> getPracticeByOCId(@RequestBody Map map, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        int organizationId = (int) map.get("organizationId");
        int chapterId = (int) map.get("chapterId");
        return testService.getPracticeByOCId(organizationId, chapterId, user.getUserId());
    }


    /***
     * 获取练习集合
     * @param map map
     *            organizationId 组织ID
     * @return 练习
     */
    @RequestMapping(value = "/practiceList", method = RequestMethod.POST)
    public RequestResult<List<Testpaper>> searchPractice(@RequestBody Map map, HttpServletRequest request) {
        String organizationId = (String) map.get("organizationId");
        if (organizationId == null || "".equals(organizationId)) {
            return new RequestResult<>(StatEnum.REQUEST_ERROR);
        }
        User user = (User) request.getSession().getAttribute("user");
        return testService.getPracticeList(Integer.parseInt(organizationId), user.getUserId());
    }

    /***
     * 获取我做过的练习列表
     * @param request request
     * @return 练习卷
     */
    @RequestMapping(value = "/getMyPractice", method = RequestMethod.POST)
    public RequestResult<List<Testpaper>> getMyPractice(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        return testService.getMyPracticeList(user.getUserId());
    }

    /***
     * 获取我做过的试卷列表
     * @param request request
     * @return 试卷
     */
    @RequestMapping(value = "/getMyTest", method = RequestMethod.POST)
    public RequestResult<List<Testpaper>> getMyTest(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        return testService.getMyTestList(user.getUserId());
    }

    /***
     * 获取问题集合
     * @param map map
     *            testpaperId 试卷ID
     * @return 试题
     */
    @RequestMapping(method = RequestMethod.POST)
    public RequestResult<List<Question>> getQuestion(@RequestBody Map map) {
        String testpaperId = (String) map.get("testpaperId");
        return testService.getQuestion(Integer.parseInt(testpaperId));
    }

    /***
     * 提交试卷
     * @param studentPaper 学生答卷
     * @return 测试结果
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public RequestResult<StudentTestResult> submit(@RequestBody StudentPaper studentPaper, HttpServletRequest request) throws ExecutionException, InterruptedException {
        if (studentPaper == null) {
            return new RequestResult<>(StatEnum.REQUEST_ERROR);
        }
        Future<RequestResult<StudentTestResult>> future = executor.submit(() -> testService.submit(studentPaper));
        RequestResult<StudentTestResult> studentTestResult = future.get();
        request.getSession().setAttribute("studentTestResult", studentTestResult.getData());
        return studentTestResult;
    }

    /***
     * 具体查看学生某道题的答题情况
     * @param map map
     *            questionId 问题ID
     * @return 详细的答题情况
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public RequestResult<StudentAnswerAnalysis> detail(@RequestBody Map map, HttpServletRequest request) {
        String questionId = (String) map.get("questionId");
        if (questionId == null || "".equals(questionId)) {
            return new RequestResult<>(StatEnum.REQUEST_ERROR);
        }

        StudentTestResult studentTestResult = (StudentTestResult) request.getSession().getAttribute("studentTestResult");
        List<StudentAnswerAnalysis> studentAnswerAnalysis = studentTestResult.getStudentAnswerAnalysis();

        for (StudentAnswerAnalysis s : studentAnswerAnalysis) {
            if (Integer.parseInt(questionId) == s.getQuestion().getQuestionId()) {
                return new RequestResult<>(StatEnum.GET_TEST_SUCCESS, s);
            }
        }
        return new RequestResult<>(StatEnum.GET_TEST_FAIL);
    }

    /***
     * 获取组织下的章节列表
     * @param map map
     *            organizationId 组织ID
     * @return 章节列表
     */
    @RequestMapping(value = "/chapter", method = RequestMethod.POST)
    public RequestResult<List<Chapter>> getChapter(@RequestBody Map map) {
        int organizationId = (int) map.get("organizationId");
        return chapterService.getByOrganizationId(organizationId);
    }

    /***
     * 添加章节
     * @param chapter 章节
     * @return 章节信息
     */
    @RequestMapping(value = "/addChapter", method = RequestMethod.POST)
    public RequestResult addChapter(@RequestBody Chapter chapter, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user.getMark() == 0) {
            return new RequestResult<>(0, "无此权限");
        }
        return chapterService.addChapter(chapter);
    }

    /***
     * 删除章节
     * @param map map
     *            chapterId 章节ID
     * @return 返回信息
     */
    @RequestMapping(value = "/deleteChapter", method = RequestMethod.POST)
    public RequestResult deleteChapter(@RequestBody Map map, HttpServletRequest request) {
        int chapterId = (int) map.get("chapterId");
        User user = (User) request.getSession().getAttribute("user");
        if (user.getMark() == 0) {
            return new RequestResult(0, "无此权限");
        }
        return chapterService.deleteChapter(chapterId);
    }
}
