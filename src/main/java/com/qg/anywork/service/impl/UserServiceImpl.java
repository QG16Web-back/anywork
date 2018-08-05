package com.qg.anywork.service.impl;

import com.qg.anywork.dao.RedisDao;
import com.qg.anywork.dao.UserDao;
import com.qg.anywork.dto.RequestResult;
import com.qg.anywork.enums.StatEnum;
import com.qg.anywork.exception.user.*;
import com.qg.anywork.model.User;
import com.qg.anywork.service.UserService;
import com.qg.anywork.utils.Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public void userMessageCheck(User user) {
        if (null == user) {
            throw new EmptyUserException("空用户对象");
        } else if (!user.getEmail().matches("\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}") ||
                !user.getUserName().matches("[a-z0-9A-Z\\u4e00-\\u9fa5]{1,15}") ||
                !user.getPassword().matches("\\w{6,15}")) {
            throw new FormatterFaultException("注册信息格式错误");
        } else {
            // 检查用户是否存在
            if (null != userDao.selectByEmail(user.getEmail())) {
                throw new UserException("该用户已经存在");
            }
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
    public RequestResult<User> login(String email, String password) {
        if (email == null || password == null) {
            throw new FormatterFaultException("空对象");
        }
        User user = userDao.selectByEmail(email);
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
        } else if (!user.getEmail().matches("\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}") ||
                !user.getUserName().matches("[a-z0-9A-Z\\u4e00-\\u9fa5]{1,15}")) {
            throw new FormatterFaultException("修改信息格式错误");
        } else {
            //置空密码
            user.setPassword("");
            userDao.updateUser(user);
            //查找新的用户实体
            User realUser = userDao.selectById(user.getUserId());
            user.setPassword("");
            return new RequestResult<>(StatEnum.INFORMATION_CHANGE_SUCCESS, realUser);
        }
    }

    @Override
    public RequestResult<User> passwordChange(User user) {
        if (user == null) {
            throw new FormatterFaultException("空用户对象");
        } else if (!user.getEmail().matches("\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}") ||
                !user.getPassword().matches("[a-z0-9A-Z\\u4e00-\\u9fa5]{1,15}")) {
            throw new FormatterFaultException("修改信息格式错误");
        } else {
            User r_user = userDao.selectByEmail(user.getEmail());
            //加密
            r_user.setPassword(Encryption.getMD5(user.getPassword()));
            userDao.updateUser(r_user);
            r_user.setPassword("");
            return new RequestResult<User>(StatEnum.INFORMATION_CHANGE_SUCCESS, null);
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
}


