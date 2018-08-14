package com.qg.anywork.model.bo;

import com.qg.anywork.util.CommonDateUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统通知实体
 *
 * @author FunriLy
 * @date 2017/7/10
 * From small beginnings comes great things.
 */
@Data
public class Message implements Serializable {

    // TODO: 2017/7/10 需要确定有哪些类型的消息

    /**
     * 通知id
     */
    private int id;

    /**
     * 发送者id
     */
    private int sendId;

    /**
     * 接收者id
     */
    private int receiveId;

    /**
     * 主要内容
     */
    private String content;

    /**
     * 类型
     */
    private int type;

    /**
     * 状态标志
     */
    private int flag;

    /**
     * 发送时间
     */
    private Date sendTime;

    public Message() {
        this.sendTime = CommonDateUtil.getNowDate();
    }

    public Message(int sendId, int receiveId, int type) {
        this.sendId = sendId;
        this.receiveId = receiveId;
        this.type = type;
    }

    public Message(int sendId, int receiveId, String content, int type) {
        this.sendId = sendId;
        this.receiveId = receiveId;
        this.content = content;
        this.type = type;
        this.sendTime = CommonDateUtil.getNowDate();
    }
}
