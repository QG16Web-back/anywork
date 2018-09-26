package com.qg.anywork.dao;

import com.github.pagehelper.Page;
import com.qg.anywork.model.po.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统通知实体操作接口
 *
 * @author FunriLy
 * @date 2017/9/25
 * From small beginnings comes great things.
 */
@Mapper
@Repository
public interface MessageDao {

    /**
     * 插入一条消息实体记录
     *
     * @param message 消息实体
     * @return int
     */
    int insertMessage(@Param("message") Message message);


    /**
     * 插入组织与公告的对应记录
     *
     * @param messageId       公告ID
     * @param organizationIds 组织ID集合
     * @return int
     */
    int insertMessageAndOrganization(@Param("messageId") int messageId, @Param("organizationIds") List<Integer> organizationIds);


    /**
     * 插入公告与用户的对应记录
     *
     * @param messageId 公告ID
     * @param userIds   用户ID集合
     * @return int
     */
    int insertUserMessage(@Param("messageId") int messageId, @Param("userIds") List<Integer> userIds);

    /**
     * 根据ID查找公告
     *
     * @param userId ID
     * @return Message列表
     */
    Page<Message> findMessageByUserId(@Param("userId") Integer userId);

    /**
     * 根据公告ID删除公告
     *
     * @param messageId 公告ID
     * @return int
     */
    int deleteMessageById(@Param("messageId") Integer messageId);

    /**
     * 删除公告与用户的对应记录
     *
     * @param messageId 公告ID
     * @return int
     */
    int deleteMessageUserByMessageId(@Param("messageId") Integer messageId);

    /**
     * 删除公告与组织的对应记录
     *
     * @param messageId 公告ID
     * @return int
     */
    int deleteMessageOrganizationByMessageId(@Param("messageId") Integer messageId);

    /**
     * 根据userId和公告Id查找
     *
     * @param userId    用户ID
     * @param messageId 公告ID
     * @return message
     */
    Message findByUserIdAndMessageId(@Param("userId") Integer userId, @Param("messageId") Integer messageId);

    /**
     * 查看已读公告
     *
     * @param messageIds 公告ID集合
     * @return 已读公告
     */
    Page<Message> findHaveReadMessageExceptMessageIds(@Param("messageIds") List<Integer> messageIds);

    /**
     * 查看未读公告
     *
     * @param messageIds 公告ID集合
     * @return 未读公告
     */
    Page<Message> findUnreadMessage(@Param("messageIds") List<Integer> messageIds);

    /**
     * 获取全部公告
     *
     * @param organizationId 组织ID
     * @return 全部公告
     */
    Page<Message> findAllMessagesByOrganizationId(@Param("organizationId") Integer organizationId);

    /**
     * 根据用户ID获取公告ID列表
     *
     * @param userId 用户ID
     * @return 公告ID列表
     */
    List<Integer> getAllMessageIdByUserId(@Param("userId") Integer userId);

    /**
     * 根据公告ID查找公告
     *
     * @param messageId 公告ID
     * @return 公告
     */
    Message findByMessageId(@Param("messageId") Integer messageId);

    /**
     * 根据用户Id和公告ID删除公告与用户对应的记录
     *
     * @param userId    用户ID
     * @param messageId 公告ID
     * @return int
     */
    int deleteMessageByUserIdAndMessageId(@Param("userId") Integer userId, @Param("messageId") Integer messageId);
}
