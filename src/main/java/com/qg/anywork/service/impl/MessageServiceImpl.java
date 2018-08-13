package com.qg.anywork.service.impl;

import com.qg.anywork.dao.MessageDao;
import com.qg.anywork.dao.OrganizationDao;
import com.qg.anywork.dao.UserDao;
import com.qg.anywork.exception.message.MessageException;
import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.enums.StatEnum;
import com.qg.anywork.exception.user.ValcodeWrongException;
import com.qg.anywork.exception.testpaper.NotPowerException;
import com.qg.anywork.exception.user.UserException;
import com.qg.anywork.model.bo.Message;
import com.qg.anywork.model.po.Organization;
import com.qg.anywork.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by ming on 18-8-5 下午10:17
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Service
public class MessageServiceImpl implements MessageService {

    /**
     * 每页数据条数
     */
    private static final int MESSAGE_NUMBER = 10;
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageDao messageDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private UserDao userDao;

    /**
     * 获取用户收到的消息
     *
     * @param userId
     * @param page
     * @param userName
     * @return
     */
    @Override
    public RequestResult<List<Message>> getReceiveMessage(int userId, int page, String userName) {
        if (page < 0) {
            throw new MessageException(StatEnum.PAGE_IS_ERROR);
        }
        int start = page * MESSAGE_NUMBER;
        int end = start + 10;
        // 获得 MESSAGE_NUMBER 条记录
        List<Message> messageList = messageDao.getReceiveMessageList(userId, start, MESSAGE_NUMBER);
        List<Message> list = new ArrayList<>();
        for (Message message : messageList) {
            try {
                list.add(replaceMessageName(userId, userName, message));
            } catch (Exception e) {
                // 不做处理，跳过该条消息
                logger.warn("用户接收消息获取未知异常：" + e.getMessage());
            }
        }
        return new RequestResult<>(StatEnum.MESSAGE_LIST, list);
    }

    /**
     * 获取用户发送的消息
     *
     * @param userId
     * @param organId
     * @param page
     * @param userName
     * @return
     */
    @Override
    public RequestResult<List<Message>> getSendMessage(int userId, int organId, int page, String userName) {
        Organization organization = organizationDao.getById(organId);
        if (organization.getTeacherId() != userId) {
            // 用户不是组织的创建者
            throw new NotPowerException(StatEnum.NOT_HAVE_POWER);
        }
        if (page < 0) {
            throw new MessageException(StatEnum.PAGE_IS_ERROR);
        }
        int start = page * MESSAGE_NUMBER;
        int end = start + 10;
        List<Message> messageList = messageDao.getSendMessageList(userId, organId, start, end);
        List<Message> list = new ArrayList<>();
        for (Message message : messageList) {
            try {
                list.add(replaceMessageName(userId, userName, message));
            } catch (Exception e) {
                logger.warn("用户发送消息获取未知异常：" + e.getMessage());
            }
        }
        return new RequestResult<>(StatEnum.MESSAGE_LIST, list);
    }

    /**
     * 将用户消息做用户名与用户id转换处理
     *
     * @param userId   用户ID
     * @param username 用户名
     * @param message  消息
     * @return 消息
     */
    private Message replaceMessageName(int userId, String username, Message message) {
        String sendName, receiveName;
        try {
            if (userId != message.getSendId()) {
                // user 不是 发送者
                sendName = userDao.selectById(message.getSendId()).getUserName();
                receiveName = username;
            } else {
                sendName = username;
                receiveName = userDao.selectById(message.getReceiveId()).getUserName();
            }
        } catch (NullPointerException e) {
            throw new UserException(StatEnum.LOGIN_NOT_EXIT_USER);
        }
        String content = message.getContent();
        content.replaceAll(String.valueOf(message.getSendId()), sendName);
        content.replaceAll(String.valueOf(message.getReceiveId()), receiveName);
        message.setContent(content);
        return message;
    }
}
