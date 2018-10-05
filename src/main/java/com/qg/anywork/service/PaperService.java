package com.qg.anywork.service;

import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.model.po.User;

import java.io.InputStream;
import java.text.ParseException;

/**
 * Create by ming on 18-10-5 下午11:07
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
public interface PaperService {

    /**
     * 添加试卷
     *
     * @param inputStream    文件输入流
     * @param testPaperTitle 试卷标题
     * @param chapterId      章节ID
     * @param createTime     开始时间
     * @param endingTime     结束时间
     * @param testPaperType  试卷类型
     * @param user           老师
     * @return result
     * @throws Exception exception
     */
    RequestResult addTestPaper(InputStream inputStream, String testPaperTitle, Integer chapterId,
                               String createTime, String endingTime, Integer testPaperType, User user) throws Exception;

    /**
     * 修改试卷信息
     *
     * @param testPaperId    试卷ID
     * @param testPaperTitle 试卷标题
     * @param testPaperType  试卷类型
     * @param createTime     开始时间
     * @param endingTime     结束时间
     * @return result
     * @throws ParseException parseException
     */
    RequestResult updateTestPaperInfo(Integer testPaperId, String testPaperTitle, Integer testPaperType,
                                      String createTime, String endingTime) throws ParseException;
}
