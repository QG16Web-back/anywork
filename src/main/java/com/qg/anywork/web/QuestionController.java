package com.qg.anywork.web;

import com.qg.anywork.enums.StatEnum;
import com.qg.anywork.exception.testpaper.NotPowerException;
import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.model.po.Question;
import com.qg.anywork.model.po.TestPaper;
import com.qg.anywork.model.po.User;
import com.qg.anywork.service.QuestionService;
import com.qg.anywork.service.TestService;
import com.qg.anywork.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author FunriLy
 * @date 2017/7/13
 * From small beginnings comes great things.
 */
@Controller
@RequestMapping("/quest")
@Slf4j
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TestService testService;

    public static void main(String[] args) {
        System.out.println(DateUtil.longToDate(1503077978826L));
    }

    /**
     * 用户上传并预览试卷/练习
     *
     * @param request request
     * @param file    file
     * @return response
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public RequestResult<List<Question>> excelUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
//        User user = (User) request.getSession().getAttribute("user");
        if (null != file && !file.isEmpty()) {
            String filename = file.getOriginalFilename();
            assert filename != null;
            if (filename.endsWith(".xlsx") || filename.endsWith(".xls")) {
//                    //文件上传
//                    FileUtils.copyInputStreamToFile(file.getInputStream(),
//                            new File(request.getServletContext().getRealPath("/excel"), user.getUserId() +".xlsx"));
//
//
//                    //读取文件
//                    String path = request.getServletContext().getRealPath("/excel"+"/"+user.getUserId()+".xlsx");
//                    System.out.println(path);
                return questionService.addQuestionList(file.getInputStream(), 41);
            }
        }
        return new RequestResult<>(StatEnum.FILE_UPLOAD_FAIL, null);
    }

    /**
     * 用户发布已经上传的试卷
     *
     * @param request        request
     * @param map            map
     *                       testpaperTitle 试卷标题
     *                       chapterId 章节ID
     *                       createTime  开始时间
     *                       endingTime  结束时间
     *                       testpaperType 试卷类型
     * @param organizationId 组织ID
     * @return request result
     */
    @PostMapping("/{organizationId}/release")
    @ResponseBody
    public RequestResult<Integer> releaseTestpaper(HttpServletRequest request, @RequestBody Map map, @PathVariable int organizationId) {
        User user = (User) request.getSession().getAttribute("user");
        int testpaperId = 0;
        try {
            if (user.getMark() != 1) {
                return new RequestResult<>(StatEnum.NOT_HAVE_POWER, 0);
            }

            TestPaper testpaper = new TestPaper();
            testpaper.setAuthorId(user.getUserId());
            testpaper.setOrganizationId(organizationId);
            testpaper.setTestpaperTitle((String) map.get("testpaperTitle"));
            testpaper.setChapterId((int) map.get("chapterId"));
            testpaper.setCreateTime(DateUtil.longToDate((Long) map.get("createTime")));
            testpaper.setEndingTime(DateUtil.longToDate((Long) map.get("endingTime")));
            testpaper.setTestpaperType((int) map.get("testpaperType"));
            //将试卷插入数据库
            testService.addTestpaper(testpaper);
            // 获得试卷ID
            testpaperId = testpaper.getTestpaperId();
            // 插入数据库并获得总分
            int socre = questionService.addTestPaper(user.getUserId(), testpaperId);
            if (testService.updateTextpaper(testpaperId, socre)) {
                //更新总分
                return new RequestResult<>(StatEnum.TEST_RELEASE_SUCESS, testpaperId);
            }
            return new RequestResult<>(StatEnum.TEST_RELEASE_FAIL, 0);
        } catch (Exception e) {
            log.warn("未知异常: ", e);
            if (testpaperId != 0) {
                //删除错误插
                questionService.deleteTestpaper(testpaperId);
            }
            return new RequestResult<>(StatEnum.DEFAULT_WRONG, 0);
        }
    }

    /**
     * 在线发布试卷
     *
     * @param request
     * @param testpaper
     * @param organizationId
     * @return
     */
    @RequestMapping(value = "/{organizationId}/submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public RequestResult<Integer> submitTestpaper(HttpServletRequest request, @RequestBody TestPaper testpaper,
                                                  @PathVariable int organizationId) {
        User user = (User) request.getSession().getAttribute("user");
        int testpaperId = 0;
        try {
            if (user.getMark() != 1) {
                return new RequestResult<>(StatEnum.NOT_HAVE_POWER, 0);
            }

            testpaper.setOrganizationId(organizationId);
            testpaper.setAuthorId(user.getUserId());
            //将试卷插入数据库
            testService.addTestpaper(testpaper);
            testpaperId = testpaper.getTestpaperId();

            int socre = questionService.addTestpaper(user.getUserId(), testpaperId, testpaper.getQuestions());
            if (testService.updateTextpaper(testpaperId, socre)) {
                //更新总分
                return new RequestResult<>(StatEnum.TEST_RELEASE_SUCESS, testpaperId);
            }
            return new RequestResult<>(StatEnum.TEST_RELEASE_FAIL, 0);
        } catch (Exception e) {
            log.warn("未知异常: ", e);
            if (testpaperId != 0) {
                //删除错误插入
                questionService.deleteTestpaper(testpaperId);
            }
            return new RequestResult<>(StatEnum.DEFAULT_WRONG, 0);
        }

    }

    /**
     * 教师导出Excel
     *
     * @param request
     * @param response
     * @param organizationId
     * @param testpaperId
     * @return
     */
    @RequestMapping(value = "/{organizationId}/export/{testpaperId}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public RequestResult<Integer> exportExcel(HttpServletRequest request, HttpServletResponse response,
                                              @PathVariable("organizationId") int organizationId, @PathVariable("testpaperId") int testpaperId) throws FileNotFoundException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return new RequestResult<>(StatEnum.USER_NOT_LOGIN, 0);
        }
        TestPaper testpaper = questionService.findTestpaperById(testpaperId);
        //权限验证
        if (user.getMark() != 1 || user.getUserId() != testpaper.getAuthorId()) {
            throw new NotPowerException(StatEnum.NOT_HAVE_POWER);
        }
        OutputStream out = new FileOutputStream(request.getServletContext().getRealPath("/excel") + "/" + user.getUserId() + ".xls");
        questionService.exportExcel(testpaperId, user.getUserId(), out);
        return new RequestResult<>(StatEnum.FILE_EXPORT_SUCCESS, testpaperId);
    }

    /**
     * 老师删除试卷
     *
     * @param request
     * @param testpaperId
     * @return
     */
    @RequestMapping(value = "/{testpaperId}/delete", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public RequestResult<?> delete(HttpServletRequest request, @PathVariable("testpaperId") int testpaperId) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        //用户未登录
        if (user == null) {
            return new RequestResult<>(StatEnum.USER_NOT_LOGIN, null);
        }
        deleteTestpaper(testpaperId, user);
        return new RequestResult<>(StatEnum.DELETE_TEST_SUCCESS);
    }

    /**
     * 当发生操作失败时，需要清除试卷和题目
     *
     * @param testpaperId 试卷Id
     */
    private void deleteTestpaper(int testpaperId, User user) {
        TestPaper testpaper = questionService.findTestpaperById(testpaperId);
        // 权限验证
        if (user.getMark() != 1 || user.getUserId() != testpaper.getAuthorId()) {
            throw new NotPowerException(StatEnum.NOT_HAVE_POWER);
        }
        questionService.deleteTestpaper(testpaperId);
    }
}
