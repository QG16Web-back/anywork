package com.qg.anywork.service.impl;

import com.qg.anywork.dao.QuestionDao;
import com.qg.anywork.dao.RedisDao;
import com.qg.anywork.dao.TestDao;
import com.qg.anywork.enums.StatEnum;
import com.qg.anywork.exception.question.ExcelReadException;
import com.qg.anywork.exception.question.QuestionException;
import com.qg.anywork.exception.question.RedisNotExitException;
import com.qg.anywork.exception.testpaper.TestPaperIsNoExit;
import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.model.po.CollectionQuestion;
import com.qg.anywork.model.po.Question;
import com.qg.anywork.model.po.TestPaper;
import com.qg.anywork.service.QuestionService;
import com.qg.anywork.util.DateUtil;
import com.qg.anywork.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

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

    @Override
    public List<Question> getQuestionList(InputStream input) {

        List<Question> list;
        try {
            list = ExcelUtil.getQuestionList(input);
        } catch (Exception e) {
            throw new ExcelReadException(StatEnum.FILE_EXPORT_FAIL);
        }
        return list;
    }

    @Override
    public RequestResult<List<Question>> addQuestionList(InputStream input, int userId) {

        List<Question> list;
        try {
            list = ExcelUtil.getQuestionList(input);
        } catch (Exception e) {
            throw new ExcelReadException(StatEnum.FILE_EXPORT_FAIL);
        }

        if (null != list) {
            // 清空并存入redis
            redisDao.removeQuestionList(userId);
            redisDao.addQuestionList(userId, new ArrayList<>(list));
        }
        return new RequestResult<>(StatEnum.FILE_READ_SUCCESS, list);
    }

    @Override
    @SuppressWarnings("unchecked")
    public int addTestPaper(int userId, int testpaperId) {
        List<Question> list;
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
            for (Question aList : list) {
                // 更新试卷号
                aList.setTestpaperId(testpaperId);
                // 插入数据库
                questionDao.insertQuestion(aList);
                // 将总分加起来
                socre += aList.getSocre();
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
            for (Question aList : list) {
                //更新试卷号
                aList.setTestpaperId(testpaperId);
                //插入数据库
                questionDao.insertQuestion(aList);
                //将总分加起来
                socre += aList.getSocre();
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
    public TestPaper findTestpaperById(int testpaperId) {
        TestPaper testpaper = testDao.getTestPaperByTestpaperId(testpaperId);
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

    @Override
    public RequestResult collectQuestion(int userId, int questionId) {
        CollectionQuestion collectionQuestion = new CollectionQuestion();
        collectionQuestion.setQuestionId(questionId);
        collectionQuestion.setStudentId(userId);
        collectionQuestion.setCollectionTime(DateUtil.format(new Date()));
        questionDao.collectQuestion(collectionQuestion);
        return new RequestResult(StatEnum.COLLECT_SUCCESS);
    }

    @Override
    public RequestResult deleteCollection(int userId, int questionId) {
        questionDao.deleteCollection(userId, questionId);
        return new RequestResult(StatEnum.DELETE_COLLECTION_SUCCESS);
    }

    @Override
    public RequestResult listCollectionQuestion(int userId) {
        List<Integer> questionIds = questionDao.findQuestionListByStudentId(userId);
        if (questionIds.size() == 0) {
            throw new QuestionException(StatEnum.COLLECTION_LIST_IS_NULL);
        }
        List<Object> data = new ArrayList<>();
        for (int questionId : questionIds) {
            HashMap<String, Object> map = new HashMap<>(2);
            map.put("questionId", questionId);
            map.put("content", questionDao.findContentById(questionId));
            data.add(map);
        }
        return new RequestResult<>(StatEnum.GET_SUCCESS, data);
    }
}
