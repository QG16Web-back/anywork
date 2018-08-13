package com.qg.anywork.service.impl;

import com.qg.anywork.dao.QuestionDao;
import com.qg.anywork.dao.RedisDao;
import com.qg.anywork.dao.TestDao;
import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.enums.StatEnum;
import com.qg.anywork.exception.question.ExcelReadException;
import com.qg.anywork.exception.question.RedisNotExitException;
import com.qg.anywork.exception.testpaper.TestPaperIsNoExit;
import com.qg.anywork.model.po.Question;
import com.qg.anywork.model.po.Testpaper;
import com.qg.anywork.service.QuestionService;
import com.qg.anywork.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by ming on 18-8-5 下午10:19
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private TestDao testDao;


    /**
     * 用户上传并读取文件
     *
     * @param input
     * @param userId
     * @return
     */
    @Override
    public RequestResult<List<Question>> addQuestionList(InputStream input, int userId) {

        List<Question> list = null;
        try {
            list = ExcelUtil.getQuestionList(input);
        } catch (Exception e) {
            throw new ExcelReadException(StatEnum.FILE_EXPORT_FAIL);
        }

        if (null != list) {
            //清空并存入redis
            redisDao.removeQuestionList(userId);
            redisDao.addQuestionList(userId, new ArrayList<>(list));
        }
        return new RequestResult<>(StatEnum.FILE_READ_SUCCESS, list);
    }

    /**
     * 发布试卷/练习
     *
     * @param userId
     * @param testpaperId
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public int addTestpaper(int userId, int testpaperId) {
        List<Question> list = null;
        int socre = 0;
        //拿到题目缓存
        list = redisDao.getQuestionList(userId);
        if (null != list) {
//            Iterator<Question> iterator = list.iterator();
//            while (iterator.hasNext()){
//                //设置试卷号
//                iterator.next().settestpaperId(testpaperId);
//            }

            // TODO: 2017/7/13 未解决事务回滚问题
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setTestpaperId(testpaperId);    //更新试卷号
                questionDao.insertQuestion(list.get(i));    //插入数据库
                socre += list.get(i).getSocre();            //将总分加起来
            }

            // TODO: 2017/7/13 MyBatis 3.3.1 修复批量插入返回主键的问题，但在SpringBoot和MyBatis3.4.0的实验中出现字段找不到的问题
            // 暂时使用遍历
            //questionDao.insertAllQuestion(list);


        } else {
            throw new RedisNotExitException(StatEnum.REDIS_CACHE_NOT_FOUND);
        }

        return socre;
    }

    @Override
    public int addTestpaper(int userId, int testpaperId, List<Question> list) {
        int socre = 0;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setTestpaperId(testpaperId);    //更新试卷号
                questionDao.insertQuestion(list.get(i));    //插入数据库
                socre += list.get(i).getSocre();            //将总分加起来
            }
        }
        return socre;
    }

    /**
     * 删除试卷
     *
     * @param testpaperId 试卷ID
     */
    @Override
    public void deleteTestpaper(int testpaperId) {
        testDao.deleteTestpaper(testpaperId);
        questionDao.deleteQuestion(testpaperId);
    }

    @Override
    public Testpaper findTestpaperById(int testpaperId) {
        Testpaper testpaper = testDao.getTestPaperByTestpaperId(testpaperId);
        if (testpaper == null) {
            throw new TestPaperIsNoExit(StatEnum.TEST_IS_NOT_EXIT);
        }
        return testpaper;
    }

    @Override
    public void exportExcel(int testpaperId, int userid, OutputStream out) throws ExcelReadException {
        List<Question> questionList = testDao.getQuestionByTestpaperId(testpaperId);
        if (questionList == null || questionList.isEmpty()) {
            throw new ExcelReadException(StatEnum.DATA_LIST_IS_NULL);
        }
        ExcelUtil.export(userid + "", questionList, out, "yyyy-MM-dd HH:mm:ss");
    }
}
