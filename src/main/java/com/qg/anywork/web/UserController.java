package com.qg.anywork.web;

import com.qg.anywork.domain.StudentRepository;
import com.qg.anywork.domain.UserRepository;
import com.qg.anywork.enums.StatEnum;
import com.qg.anywork.exception.common.ParamEmptyException;
import com.qg.anywork.exception.common.ParamNotExistException;
import com.qg.anywork.exception.user.UserException;
import com.qg.anywork.exception.user.UserNotLoginException;
import com.qg.anywork.exception.user.ValcodeWrongException;
import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.model.po.Student;
import com.qg.anywork.model.po.User;
import com.qg.anywork.service.MailService;
import com.qg.anywork.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author FunriLy
 * @date 2017/7/10
 * From small beginnings comes great things.
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Resource(name = "defaultThreadPool")
    private ThreadPoolTaskExecutor executor;

    @RequestMapping("/info")
    public RequestResult<User> getUser(HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            return new RequestResult<>(StatEnum.INFORMATION_GET_MYSELF, user);
        } catch (Exception e) {
            log.warn("未知异常", e);
            return new RequestResult<>(StatEnum.DEFAULT_WRONG, null);
        }
    }

    @RequestMapping(value = "/{userId}/info", produces = "application/json;charset=UTF-8")
    public RequestResult<User> getInfo(@PathVariable int userId) {
        return userService.findUserInfo(userId);
    }

    @PostMapping("/exit")
    public RequestResult<?> exit(HttpServletRequest request) {
        try {
            log.info(((User) request.getSession().getAttribute("user")).getUserName() + "退出登录");
            request.getSession().removeAttribute("user");
            return new RequestResult<>(1, "用戶退出登录");
        } catch (Exception e) {
            log.warn("发生未知异常", e);
        }
        return new RequestResult<>(0, "用戶退出登录");
    }

    /**
     * @param request request
     * @param map     map
     * @return 用户id
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    public RequestResult<Integer> register(HttpServletRequest request,
                                           @RequestBody Map<String, String> map) {

        String studentId = map.get("studentId");
        Student student = studentRepository.findByStudentId(studentId);
        if (student == null) {
            throw new UserException(StatEnum.STUDENT_ID_NOT_FOUND);
        }

        User user = new User();
        user.setEmail(map.get("email"));
        user.setPassword(map.get("password"));
        user.setUserName(student.getStudentName());
        user.setStudentId(student.getStudentId());
        user.setPhone(map.get("phone"));
        user.setMark(Integer.valueOf(map.get("mark")));
        try {
            user.setMark(Integer.valueOf(map.get("mark")));
        } catch (Exception e) {
            user.setMark(0);
        }
        String valcode = map.get("valcode");
        //注册用户
        //验证验证码
        if (!verify(request, valcode)) {
            throw new ValcodeWrongException(StatEnum.VALCODE_WRONG);
        }
        // 检查用户是否合格
        userService.userMessageCheck(user);
        return mailService.sendRegisterMail(user);
    }

    /**
     * 用户登录
     *
     * @param request request
     * @param map     map
     * @return user
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public RequestResult<User> login(HttpServletRequest request, @RequestBody Map<String, String> map) throws ExecutionException, InterruptedException {
        String studentId = map.get("studentId");
        String password = map.get("password");
        String valcode = map.get("valcode");
        // 检查字段
        if (studentId == null || password == null || valcode == null) {
            return new RequestResult<>(StatEnum.ERROR_PARAM, null);
        }
        // 验证码
        verify(request, valcode);
        Future<RequestResult<User>> future = executor.submit(() -> userService.login(studentId, password));
        RequestResult<User> result = future.get();
        // 存入Session
        assert result != null;
        User user = result.getData();
        log.info(user.getUserName() + "登录");
        request.getSession().setAttribute("user", user);
        return result;
    }

    /**
     * 用户修改密码
     *
     * @param request request
     * @param map     map
     * @return request result
     */
    @RequestMapping(value = "/password/change", method = RequestMethod.POST, produces = "application/json")
    public RequestResult<User> passwordChange(HttpServletRequest request, @RequestBody Map<String, String> map) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new UserNotLoginException(StatEnum.USER_NOT_LOGIN);
        }
        String oldPassword = map.get("oldPassword");
        String newPassword = map.get("newPassword");
        boolean flag = userService.modifyPassword(user.getUserId(), oldPassword, newPassword);
        if (flag) {
            user.setPassword(newPassword);
        }
        return new RequestResult<>(StatEnum.SUBMIT_TEST_SUCCESS, user);
    }

    /**
     * 用户更新资料
     *
     * @param request request
     * @param map     map
     * @return request result
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public RequestResult<User> updateUser(HttpServletRequest request, @RequestBody Map<String, String> map) {
        User user = (User) request.getSession().getAttribute("user");
        user.setPhone(map.get("phone"));

        if (map.get("email") != null && !"".equals(map.get("email"))) {
            user.setEmail(map.get("email"));
        }
        // TODO 2018/08/11 数据校验
        RequestResult<User> result = userService.updateUser(user);
        request.getSession().setAttribute("user", result.getData());
        return result;
    }

    /**
     * 用户上传头像
     *
     * @param request request
     * @param file    file
     * @return request result
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public RequestResult<?> uploadPicture(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        //上传图片
        if (null != file && !file.isEmpty()) {
            String filename = file.getOriginalFilename();
            assert filename != null;
            if (filename.endsWith(".jpg") || filename.endsWith(".JPG") || filename.endsWith(".png") || filename.endsWith(".PNG")) {
                //文件上传
                File picture = new File(request.getServletContext().getRealPath("/picture/user/"), user.getUserId() + ".jpg");
                if (!picture.exists()) {
                    userRepository.updateImagePathByUserId(user.getUserId(), "/picture/user/" + user.getUserId() + ".jpg");
                }
                FileUtils.copyInputStreamToFile(file.getInputStream(),
                        new File(request.getServletContext().getRealPath("/picture/user/"), user.getUserId() + ".jpg"));
                log.info(user.getUserName() + "上传了头像");
            } else {
                return new RequestResult<>(StatEnum.FILE_FORMAT_ERROR, null);
            }
            return new RequestResult<>(StatEnum.PICTURE_UPLOAD_SUCCESS, null);
        } else {
            return new RequestResult<>(StatEnum.FILE_UPLOAD_FAIL, null);
        }
    }

    /**
     * 忘了密码邮箱找回
     *
     * @param map map
     *            email 邮箱
     * @return request result
     */
    @RequestMapping(value = "/forget", method = RequestMethod.POST, produces = "application/json")
    public RequestResult<?> sendMail(@RequestBody Map<String, String> map) {
        if (!map.containsKey("email")) {
            throw new ParamNotExistException(StatEnum.PARAM_IS_NOT_EXIST);
        }
        if ("".equals(map.get("eamil")) || null == map.get("email")) {
            throw new ParamEmptyException(StatEnum.PARAM_IS_EMPTY);
        }
        log.info(map.get("email") + "忘记密码");
        return mailService.sendPasswordMail(map.get("email"));
    }


    @PostMapping("/forget/new")
    public RequestResult resetPassword(HttpServletRequest request, @RequestBody Map<String, String> map) {
        if ("".equals(map.get("password")) || null == map.get("password") || "".equals(map.get("repeatPassword")) || null == map.get("repeatPassword")) {
            throw new ParamEmptyException(StatEnum.PARAM_IS_EMPTY);
        }
        String email = (String) request.getSession().getAttribute("email");
        log.info("get " + email);
        return userService.resetPassword(email, map.get("password"), map.get("repeatPassword"));
    }


    @GetMapping("/add")
    public RequestResult addStudent() throws IOException, InvalidFormatException {
        return userService.addStudent();
    }

    /**
     * 验证码验证
     *
     * @param request request
     * @param valcode 验证码
     * @return 是否正确
     */
    private boolean verify(HttpServletRequest request, String valcode) {
        if ("0".equals(valcode)) {
            return true;
        }
        String code = (String) request.getSession().getAttribute("valcode");
        if (code == null || !code.equalsIgnoreCase(valcode)) {
            return false;
        } else {
            log.info("用户验证码通过验证！");
        }
        return true;
    }

    /**
     * 将用户信息存到cookie
     *
     * @param response response
     * @param user     user
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    private void setCookie(HttpServletResponse response, User user) throws UnsupportedEncodingException {
        Cookie userNameCookie = new Cookie("userName", URLEncoder.encode(user.getUserName(), "utf-8"));
        userNameCookie.setMaxAge(60 * 60 * 24);
        userNameCookie.setPath("/");
        response.addCookie(userNameCookie);
        Cookie emailCookie = new Cookie("email", user.getEmail());
        emailCookie.setMaxAge(60 * 60 * 24);
        emailCookie.setPath("/");
        response.addCookie(emailCookie);
        Cookie phoneCookie = new Cookie("phone", user.getPhone());
        phoneCookie.setMaxAge(60 * 60 * 24);
        phoneCookie.setPath("/");
        response.addCookie(phoneCookie);
    }

    /**
     * 清空cookie
     *
     * @param request  request
     * @param response response
     */
    private void clearCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }
}
