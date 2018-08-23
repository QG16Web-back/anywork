package com.qg.anywork.service;

import com.qg.anywork.exception.question.ExcelReadException;
import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.model.po.Question;
import com.qg.anywork.model.po.TestPaper;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by FunriLy on 2017/7/13.
 * From small beginnings comes great things.
 */
public interface QuestionService {

    /**
     * 通过excel文件读取问题列表
     * @param input
     * @return
     */
    List<Question> getQuestionList(InputStream input);

    /**
     * 教师上传试卷并读取文件
     *
     * @param input  文件输入流
     * @param userId 用户ID
     * @return 试卷列表
     */
    RequestResult<List<Question>> addQuestionList(InputStream input, int userId);

    /**
     * 发布试卷/练习
     *
     * @param userId      用户ID
     * @param testpaperId 试卷ID
     * @return int
     */
    int addTestPaper(int userId, int testpaperId);

    /**
     * 发布试卷/练习
     *
     * @param userId      用户ID
     * @param testpaperId 试卷ID
     * @param list        题目列表
     * @return int
     */
    int addTestpaper(int userId, int testpaperId, List<Question> list);

    /**
     * 删除试卷
     *
     * @param testpaperId 试卷ID
     */
    void deleteTestpaper(int testpaperId);

    TestPaper findTestpaperById(int testpaperId);

    void exportExcel(int testpaperId, int userid, OutputStream out) throws ExcelReadException;
}
