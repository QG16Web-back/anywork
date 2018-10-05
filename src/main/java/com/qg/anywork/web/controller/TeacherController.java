package com.qg.anywork.web.controller;

import com.qg.anywork.model.bo.TeacherSubmit;
import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.model.po.CheckResult;
import com.qg.anywork.model.po.StudentTestResult;
import com.qg.anywork.model.po.User;
import com.qg.anywork.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author logan
 * @date 2017/7/31
 */
@RestController
@RequestMapping("/teacher")
@Slf4j
public class TeacherController {

    @Autowired
    private TestService testService;

    /**
     * 获取某套题组织内成员的完成情况
     * @param map map
     *            testpaperId 试卷ID
     *            organizationId 组织ID
     * @return 试卷完成情况
     */
    @RequestMapping(value = "/lookTest", method = RequestMethod.POST)
    public RequestResult<List<CheckResult>> studentTest(@RequestBody Map map) {
        int testpaperId = (int) map.get("testpaperId");
        int organizationId = (int) map.get("organizationId");
        return testService.getCheckResultByTestpaperId(organizationId, testpaperId);
    }


    /***
     * 老师改卷
     */
    @RequestMapping(value = "/judge", method = RequestMethod.POST)
    public RequestResult<StudentTestResult> submit(@RequestBody TeacherSubmit teacherSubmit, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user.getMark() == 0) {
            return new RequestResult<>(0, "权限不足");
        }
        testService.updateStudentTest(teacherSubmit);
        return testService.getDetail(teacherSubmit.getTestpaperId(), teacherSubmit.getStudentId());
    }
}
