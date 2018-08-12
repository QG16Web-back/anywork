package com.qg.anywork.service;

import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.exception.question.ExcelReadException;
import com.qg.anywork.model.po.Question;
import com.qg.anywork.model.po.Testpaper;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by FunriLy on 2017/7/13.
 * From small beginnings comes great things.
 */
public interface QuestionService {

    /**
     * 用户上传并读取文件
     *
     * @param input
     * @param userId
     * @return
     */
    RequestResult<List<Question>> addQuestionList(InputStream input, int userId);

    /**
     * 发布试卷/练习
     *
     * @param userId
     * @param testpaperId
     * @return
     */
    int addTestpaper(int userId, int testpaperId);

    int addTestpaper(int userId, int testpaperId, List<Question> list);

    /**
     * 删除试卷
     *
     * @param testpaperId 试卷ID
     */
    void deleteTestpaper(int testpaperId);

    Testpaper findTestpaperById(int testpaperId);

    void exportExcel(int testpaperId, int userid, OutputStream out) throws ExcelReadException;
}
