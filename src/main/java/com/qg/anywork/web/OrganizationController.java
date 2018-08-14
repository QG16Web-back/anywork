package com.qg.anywork.web;

import com.qg.anywork.enums.StatEnum;
import com.qg.anywork.model.bo.StudentTestResult;
import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.model.po.CheckResult;
import com.qg.anywork.model.po.Organization;
import com.qg.anywork.model.po.User;
import com.qg.anywork.service.OrganizationService;
import com.qg.anywork.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 组织模块
 *
 * @author logan
 * @date 2017/7/11
 */
@RestController
@RequestMapping("/organization")
@Slf4j
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private TestService testService;

    /***
     * 根据组织关键字模糊查找
     *
     * @param request request
     * @param map map
     *            organizationName 组织关键字
     * @return request result
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public RequestResult<List<Organization>> search(HttpServletRequest request, @RequestBody Map map) {
        User user = (User) request.getSession().getAttribute("user");
        String organizationName = (String) map.get("organizationName");
        return organizationService.search(organizationName, user.getUserId());
    }

    /***
     * 加入组织
     *
     * @param request request
     * @param map map
     *            organizationId 组织ID
     *            token 口令
     * @return request result
     */
    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public RequestResult<?> join(HttpServletRequest request, @RequestBody Map map) {
        User user = (User) request.getSession().getAttribute("user");
        String organizationId = (String) map.get("organizationId");
        String token = (String) map.get("token");
        return organizationService.join(Integer.parseInt(organizationId), Long.parseLong(token), user.getUserId());
    }

    /***
     * 获取我的组织列表
     *
     * @param request request
     * @return request result
     */
    @RequestMapping(value = "/me", method = RequestMethod.POST)
    public RequestResult<List<Organization>> myOrganization(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return new RequestResult<>(StatEnum.ORGAN_SEARCH_FAIL);
        }
        return organizationService.searchByUserId(user.getUserId());
    }

    /***
     * 退出组织接口
     *
     * @param request request
     * @param map map
     *            organizationId 组织ID
     * @return request request
     */
    @RequestMapping(value = "/leave", method = RequestMethod.POST)
    public RequestResult leave(HttpServletRequest request, @RequestBody Map map) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return new RequestResult(StatEnum.ORGAN_SEARCH_FAIL);
        }
        int organizationId = (int) map.get("organizationId");
        return organizationService.exitOrganization(organizationId, user.getUserId());
    }

    /***
     * 创建组织
     * @param file             file 组织头像
     * @param organizationName 组织名称
     * @param description      描述
     * @param request          request
     * @return request result
     * @throws IOException ioException
     */
    @PostMapping("/create")
    public RequestResult addOrganization(@RequestPart("file") MultipartFile file,
                                         @RequestParam("organizationName") String organizationName,
                                         @RequestParam("description") String description,
                                         HttpServletRequest request) throws IOException {
        if (description == null || "".equals(description)) {
            return new RequestResult<>(0, "描述为空");
        }
        if (organizationName == null || "".equals(organizationName)) {
            return new RequestResult<>(0, "组织名为空");
        }
        User user = (User) request.getSession().getAttribute("user");
        user.setUserId(289);
        if (user.getMark() == 0) {
            return new RequestResult<>(0, "没有权限");
        }

        Organization o = new Organization();
        o.setTeacherName(user.getUserName());
        o.setTeacherId(user.getUserId());
        o.setDescription(description);
        o.setOrganizationName(organizationName);
        if (organizationService.getByName(organizationName) != null) {
            return new RequestResult<>(0, "组织名称已存在");
        }

        if (file != null && !file.isEmpty()) {
            String filename = file.getOriginalFilename();
            assert filename != null;
            if (!(filename.endsWith(".jpg") || filename.endsWith(".JPG") || filename.endsWith(".png") || filename.endsWith(".PNG"))) {
                return new RequestResult<>(0, "上传的头像不合法");
            }

            String photoName = o.getOrganizationName() + ".jpg";
            FileUtils.copyInputStreamToFile(file.getInputStream(),
                    new File(request.getServletContext().getRealPath("/picture/organization"), photoName));
        }

        return organizationService.addOrganization(o);
    }


    /***
     * 修改组织信息
     * @param file file 组织头像
     * @param organizationId 组织ID
     * @param organizationName 组织名称
     * @param description 描述
     * @param request request
     * @return request result
     * @throws IOException ioException
     */
    @RequestMapping(value = "/alter", method = RequestMethod.POST)
    public RequestResult alter(@RequestParam(value = "file", required = false) MultipartFile file,
                               @RequestParam(value = "organizationId", required = true) int organizationId,
                               @RequestParam(value = "organizationName", required = false) String organizationName,
                               @RequestParam(value = "description", required = false) String description,
                               HttpServletRequest request) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user.getMark() == 0) {
            return new RequestResult<>(0, "没有权限");
        }

        Organization o = organizationService.getById(organizationId);
        if (o.getTeacherId() != user.getUserId()) {
            return new RequestResult<>(0, "您不是此组织的创建人");
        }
        boolean flag = (organizationName != null && !"".equals(organizationName)) || (description != null && !"".equals(description));
        if (flag) {

            if ((description != null && !"".equals(description))) {
                o.setDescription(description);
            }
            if ((organizationName != null && !"".equals(organizationName))) {
                if (organizationService.getByName(organizationName) != null) {
                    return new RequestResult<>(0, "组织名称已存在");
                }
                o.setOrganizationName(organizationName);
            }
            if (file != null && !file.isEmpty()) {
                String filename = file.getOriginalFilename();
                assert filename != null;
                if (!(filename.endsWith(".jpg") || filename.endsWith(".JPG") || filename.endsWith(".png") || filename.endsWith(".PNG"))) {
                    return new RequestResult(0, "上传的头像不合法");
                }

                String photoName = organizationName + ".jpg";
                FileUtils.copyInputStreamToFile(file.getInputStream(),
                        new File(request.getServletContext().getRealPath("/picture/organization"), photoName));
            }
            organizationService.alterOrganization(o);
        }
        return new RequestResult<>(1, "修改成功", o);
    }

    /***
     * 删除组织
     * @param request request
     * @param map map
     *            organizationId 组织ID
     * @return request result
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RequestResult delete(HttpServletRequest request, @RequestBody Map map) {
        int organizationId = (int) map.get("organizationId");
        User user = (User) request.getSession().getAttribute("user");
        if (user.getMark() == 0) {
            return new RequestResult(0, "无此权限");
        }
        return organizationService.deleteOrganization(organizationId, user.getUserId());
    }

    /***
     * 查看我创建过的组织
     * @param request request
     * @return request result
     */
    @RequestMapping(value = "/myOrganization", method = RequestMethod.POST)
    public RequestResult<List<Organization>> listMyOrganization(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user.getMark() == 0) {
            return new RequestResult<>(0, "无此权限");
        }
        return organizationService.getMyOrganization(user.getUserId());
    }


    /***
     * 获得该组织下的成员
     * @param map map
     *            organizationId 组织ID
     * @return 组织成员
     */
    @RequestMapping(value = "/student", method = RequestMethod.POST)
    public RequestResult<List<User>> getStudentOfOrganization(@RequestBody Map map) {
        int organizationId = (int) map.get("organizationId");
        return organizationService.getOrganizationPeople(organizationId);
    }

    /***
     * 获取试卷详情
     * @param map map
     *            organizationId 组织ID
     *            userId 用户ID
     * @return 试卷详情
     */
    @RequestMapping(value = "/studentTestDetail", method = RequestMethod.POST)
    public RequestResult<StudentTestResult> getDetail(@RequestBody Map map) {
        int testpaperId = (int) map.get("testpaperId");
        int userId = (int) map.get("userId");
        return testService.getDetail(testpaperId, userId);
    }

    /***
     * 获取组织下某学生的考试列表
     * @param map map
     * @return 学生的考试列表
     */
    @RequestMapping(value = "/studentTest", method = RequestMethod.POST)
    public RequestResult<List<CheckResult>> studentTest(@RequestBody Map map) {
        int userId = (int) map.get("userId");
        int organizationId = (int) map.get("organizationId");
        return testService.getCheckResultByUser(organizationId, userId);
    }

    /***
     * 获取组织下某学生的考试列表
     * @param map map
     *            organizationId 组织ID
     *            userId 用户ID
     * @return 试卷列表
     */
    @RequestMapping(value = "/studentPracetice", method = RequestMethod.POST)
    public RequestResult<List<CheckResult>> studentPracetice(@RequestBody Map map) {
        int userId = (int) map.get("userId");
        int organizationId = (int) map.get("organizationId");
        return testService.getPraceticeByOrganizationId(userId, organizationId);
    }


}
