package com.qg.anywork.service.impl;

import com.qg.anywork.dao.RedisDao;
import com.qg.anywork.dao.UserDao;
import com.qg.anywork.domain.StudentRepository;
import com.qg.anywork.domain.UserRepository;
import com.qg.anywork.enums.StatEnum;
import com.qg.anywork.exception.user.*;
import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.model.po.Student;
import com.qg.anywork.model.po.User;
import com.qg.anywork.service.UserService;
import com.qg.anywork.util.Encryption;
import com.qg.anywork.util.ExcelUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by ming on 18-8-5 上午11:20
 * <p>
 * 用户服务实现类
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisDao redisDao;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void userMessageCheck(User user) {
        if (null == user) {
            throw new EmptyUserException("空用户对象");
        } else if (userRepository.findByStudentId(user.getStudentId()) != null) {
            throw new UserException("该学号已经被注册");
        } else if (!user.getEmail().matches("\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}") ||
                !user.getPassword().matches("\\w{6,15}")) {
            throw new FormatterFaultException("注册信息格式错误");
        } else if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserException("该邮箱已经被注册");
        }
    }

    @Override
    public void register(String email) {
        User user;
        user = redisDao.getUserMessage(email);
        if (null == user) {
            throw new EmptyUserException("空用户对象");
        } else if (!user.getEmail().matches("\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}") ||
                !user.getUserName().matches("[a-z0-9A-Z\\u4e00-\\u9fa5]{1,15}") ||
                !user.getPassword().matches("\\w{6,15}")) {
            throw new FormatterFaultException("注册信息格式错误");
        } else {
            if (null != userDao.selectByEmail(user.getEmail())) {
                throw new UserException("该用户已经存在");
            }
            //格式正确，加密密码并存入数据库
            user.setPassword(Encryption.getMD5(user.getPassword()));
            userDao.insertUser(user);
            int userId = user.getUserId();
            if (userId <= 0) {
                throw new UserException("未知错误");
            }
        }
    }

    @Override
    public RequestResult<User> login(String studentId, String password) {
        if (studentId == null || password == null) {
            throw new FormatterFaultException("空对象");
        }
        User user = userRepository.findByStudentId(studentId);
        System.out.println(user);
        if (user == null) {
            throw new UserNotExitException("不存在的用户");
        } else if (!user.getPassword().equals(Encryption.getMD5(password))) {
            throw new UserLoginFailException("错误的用户名或密码");
        } else {
            //登录成功
            // TODO: 2017/7/10 用户登录成功，检索出所有的组织
            user.setPassword("");
            return new RequestResult<>(StatEnum.LOGIN_SUCCESS, user);
        }
    }

    @Override
    public RequestResult<User> updateUser(User user) {
        if (user == null) {
            throw new FormatterFaultException("空用户对象");
        } else if (!user.getUserName().matches("[a-z0-9A-Z\\u4e00-\\u9fa5]{1,15}")) {
            throw new FormatterFaultException("修改信息格式错误");
        } else if (!"".equals(user.getPhone())) {
            if (user.getPhone().matches("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\\\d{8}$")) {
                throw new FormatterFaultException("修改信息格式错误");
            }
        } else{
            //置空密码
            user.setPassword("");
            userDao.updateUser(user);
            //查找新的用户实体
            User realUser = userDao.selectById(user.getUserId());
            user.setPassword("");
            return new RequestResult<>(StatEnum.INFORMATION_CHANGE_SUCCESS, realUser);
        }
        return null;
    }

    @Override
    public boolean modifyPassword(int userId, String oldPassword, String newPassword) {
        User user = userRepository.findByUserId(userId);
        if (!user.getPassword().equals(Encryption.getMD5(oldPassword))) {
            throw new UserException("原密码输入错误");
        }
        if (!newPassword.matches("[a-z0-9A-Z\\u4e00-\\u9fa5]{1,15}")) {
            throw new FormatterFaultException("修改信息格式错误");
        } else {
            userRepository.updatePasswordByUserId(userId, Encryption.getMD5(newPassword));
            return true;
        }
    }

    @Override
    public RequestResult<User> findUserInfo(int userId) {
        User user = userDao.selectById(userId);
        if (null == user) {
            throw new UserNotExitException("不存在的用户");
        }
        user.setPassword("");
        return new RequestResult<>(StatEnum.INFORMATION_GET_MYSELF, user);
    }

    @Override
    public RequestResult addStudent() {
        String path = System.getProperty("user.dir") + "/src/main/resources";
        File file = new File(path + "/static/名单.xlsx");
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<Student> students = new ArrayList<>();
        try {
            students = ExcelUtil.readStudentExcel(inputStream);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
        studentRepository.saveAll(students);
        return new RequestResult(1, "添加成功");
    }
}


