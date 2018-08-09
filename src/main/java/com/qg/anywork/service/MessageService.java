package com.qg.anywork.service;

import com.qg.anywork.dto.RequestResult;
import com.qg.anywork.model.Message;
import java.util.List;

/**
 * 消息实体 Service 层
 *
 * @author FunriLy
 * @date 2017/9/25
 * From small beginnings comes great things.
 */
public interface MessageService {

    /**
     * 获取用户收到的消息
     *
     * @param userId
     * @param page
     * @param userName
     * @return
     */
    RequestResult<List<Message>> getReceiveMessage(int userId, int page, String userName);

    /**
     * 获取用户发送的消息
     *
     * @param userId
     * @param organId
     * @param page
     * @param userName
     * @return
     */
    RequestResult<List<Message>> getSendMessage(int userId, int organId, int page, String userName);
}
