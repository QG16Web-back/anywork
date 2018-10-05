package com.qg.anywork.service.impl;

import com.qg.anywork.dao.OrganizationDao;
import com.qg.anywork.dao.PaperDao;
import com.qg.anywork.dao.QuestionDao;
import com.qg.anywork.enums.StatEnum;
import com.qg.anywork.exception.testpaper.TestPaperTimeException;
import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.model.po.Question;
import com.qg.anywork.model.po.TestPaper;
import com.qg.anywork.model.po.User;
import com.qg.anywork.service.PaperService;
import com.qg.anywork.util.DateUtil;
import com.qg.anywork.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Create by ming on 18-10-5 下午11:07
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PaperServiceImpl implements PaperService {

    private final OrganizationDao organizationDao;

    private final PaperDao paperDao;

    private final QuestionDao questionDao;

    @Autowired
    public PaperServiceImpl(OrganizationDao organizationDao, PaperDao paperDao, QuestionDao questionDao) {
        this.organizationDao = organizationDao;
        this.paperDao = paperDao;
        this.questionDao = questionDao;
    }

    @Override
    public RequestResult addTestPaper(InputStream inputStream, String testPaperTitle, Integer chapterId,
                                      String createTime, String endingTime, Integer testPaperType, User user) throws Exception {
        if (DateUtil.parse(createTime).after(DateUtil.parse(endingTime)) || DateUtil.parse(createTime).after(new Date())) {
            // 开始时间比结束时间晚
            throw new TestPaperTimeException(StatEnum.TEST_PAPER_TIME_ERROR);
        }
        List<Integer> organizationIds = organizationDao.getMyOrganizationIds(user.getUserId());
        int testPaperId;
        TestPaper testpaper = new TestPaper();
        testpaper.setAuthorId(user.getUserId());
        testpaper.setTestpaperTitle(testPaperTitle);
        testpaper.setChapterId(chapterId);
        testpaper.setCreateTime(DateUtil.parse(createTime));
        testpaper.setEndingTime(DateUtil.parse(endingTime));
        testpaper.setTestpaperType(testPaperType);
        // 将试卷插入数据库
        paperDao.insertTestPaper(testpaper);
        // 获得试卷ID
        testPaperId = testpaper.getTestpaperId();
        // 插入试卷与组织的关系
        paperDao.insertTestPaperOrganization(testPaperId, organizationIds);

        List<Question> questionList = ExcelUtil.getQuestionList(inputStream);

        int score = 0;
        if (questionList != null && questionList.size() > 0) {
            for (Question question : questionList) {
                //更新试卷号
                question.setTestpaperId(testPaperId);
                //将总分加起来
                score += question.getSocre();
            }
        }
        questionDao.insertAllQuestion(questionList);
        paperDao.updateTestPaperScore(testPaperId, score);
        testpaper.setQuestions(questionList);
        testpaper.setTestpaperScore(score);
        log.info("{}发布了试卷", user.getUserName());
        return new RequestResult<>(StatEnum.TEST_RELEASE_SUCESS, testpaper);
    }

    @Override
    public RequestResult updateTestPaperInfo(Integer testPaperId, String testPaperTitle, Integer testPaperType, String createTime, String endingTime) throws ParseException {
        TestPaper testPaper = new TestPaper();
        testPaper.setTestpaperId(testPaperId);
        testPaper.setTestpaperTitle(testPaperTitle);
        testPaper.setTestpaperType(testPaperType);
        if (!(createTime == null || "".equals(createTime))) {
            testPaper.setCreateTime(DateUtil.parse(createTime));
        }
        if (!(endingTime == null || "".equals(endingTime))) {
            testPaper.setEndingTime(DateUtil.parse(endingTime));
        }
        paperDao.updateTestPaperInfo(testPaper);
        testPaper = paperDao.findByTestPaperId(testPaperId);
        return new RequestResult<>(0, "修改成功", testPaper);
    }
}
