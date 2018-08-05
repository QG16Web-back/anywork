package com.qg.anywork.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 试卷实体
 * Created by FunriLy on 2017/7/10.
 * From small beginnings comes great things.
 */
@Data
@NoArgsConstructor
public class Testpaper {

    /**
     * 试卷ID
     */
    private int testpaperId;

    /**
     * 试卷标题
     */
    private String testpaperTitle;

    /**
     * 教师ID
     */
    private int authorId;

    /**
     * 组织ID
     */
    private int organizationId;

    /**
     * 开始时间
     */
    private Date createTime;

    /**
     * 结束时间
     */
    private Date endingTime;

    /**
     * 章节id，为练习卷添加章节  -1 没做过 0 做过
     */
    private int chapterId;

    /**
     * 章节名称，为练习卷添加章节
     */
    private String chapterName;

    /**
     * 试卷分数
     */
    private int testpaperScore;

    /**
     * 试卷类型，0为练习、1为考试，若将来扩展可在这个字段上实现
     */
    private int testpaperType;

    // TODO 为了让老师上传试卷时可以获得题目
    /**
     * 试卷下的所有题目
     */
    private List<Question> questions;
}
