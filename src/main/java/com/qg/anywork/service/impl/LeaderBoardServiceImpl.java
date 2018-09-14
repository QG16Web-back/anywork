package com.qg.anywork.service.impl;

import com.qg.anywork.dao.*;
import com.qg.anywork.enums.StatEnum;
import com.qg.anywork.exception.common.ParamException;
import com.qg.anywork.model.dto.LeaderBoard;
import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.model.po.Organization;
import com.qg.anywork.model.po.StudentTestResult;
import com.qg.anywork.model.po.TestPaper;
import com.qg.anywork.model.po.User;
import com.qg.anywork.service.LeaderBoardService;
import com.qg.anywork.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create by ming on 18-9-13 下午12:56
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Service
@Slf4j
@SuppressWarnings("unchecked")
public class LeaderBoardServiceImpl implements LeaderBoardService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TestPaperDao testPaperDao;

    @Autowired
    private TestDao testDao;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private LeaderBoardRedisDao leaderBoardRedisDao;

    @Override
    public RequestResult showLeaderBoard(int userId, int leaderBoardType) {
        List<LeaderBoard> leaderBoards;
        int organizationId = userDao.findOrganizationIdByUserId(userId);
        if (leaderBoardType == 1) {
            leaderBoards = showLeaderBoardByOrganization(organizationId);
        } else if (leaderBoardType == 2) {
            leaderBoards = showLeaderBoardByTeacher(organizationId);
        } else {
            throw new ParamException(StatEnum.ERROR_PARAM);
        }
        return new RequestResult<>(StatEnum.GET_SUCCESS, leaderBoards);
    }

    @Override
    public RequestResult showPaperLeaderBoard(int userId, int leaderBoardType, int testPaperId) {
        List<LeaderBoard> leaderBoards;
        int organizationId = userDao.findOrganizationIdByUserId(userId);
        if (leaderBoardType == 1) {
            leaderBoards = showPaperLeaderBoardByOrganization(testPaperId, organizationId);
        } else if (leaderBoardType == 2) {
            leaderBoards = showPaperLeaderBoardByTeacher(testPaperId, organizationId);
        } else {
            throw new ParamException(StatEnum.ERROR_PARAM);
        }
        return new RequestResult<>(StatEnum.GET_SUCCESS, leaderBoards);
    }

    private List<LeaderBoard> showPaperLeaderBoardByOrganization(int testPaperId, int organizationId) {
        List<User> users;
        String key = "showPaperLeaderBoardByOrganization" + "_" + testPaperId + "_" + organizationId;
        List<LeaderBoard> redisLeaderBoards = (List<LeaderBoard>) leaderBoardRedisDao.getLeaderBoard(key);
        if (redisLeaderBoards != null) {
            return redisLeaderBoards;
        }
        users = userDao.findUserByOrganizationId(organizationId);
        String organizationName = organizationDao.getById(organizationId).getOrganizationName();
        List<LeaderBoard> leaderBoards = new ArrayList<>();
        for (User user : users) {
            LeaderBoard leaderBoard = new LeaderBoard();
            leaderBoard.setImagePath(user.getImagePath());
            leaderBoard.setOrganizationName(organizationName);
            leaderBoard.setStudentId(user.getStudentId());
            leaderBoard.setUsername(user.getUserName());
            StudentTestResult testResult = testDao.findTestResultByTestPaperIdAndUserIdAndOrganizationId(testPaperId, user.getUserId());
            if (testResult == null) {
                leaderBoard.setScore(0.0);
            } else {
                leaderBoard.setScore(testResult.getSocre());
            }
            leaderBoards.add(leaderBoard);
        }
        leaderBoards.sort((o1, o2) -> Double.compare(o2.getScore(), o1.getScore()));
        // 存进缓存
        leaderBoardRedisDao.setLeaderBoard(key, leaderBoards);
        return leaderBoards;
    }

    private List<LeaderBoard> showPaperLeaderBoardByTeacher(int testPaperId, int organizationId) {
        String key = "showPaperLeaderBoardByTeacher" + "_" + testPaperId + "_" + organizationId;
        List<LeaderBoard> redisLeaderBoards = (List<LeaderBoard>) leaderBoardRedisDao.getLeaderBoard(key);
        if (redisLeaderBoards != null) {
            return redisLeaderBoards;
        }
        List<Organization> organizations = organizationDao.findOrganizationById(organizationId);
        List<LeaderBoard> leaderBoards = new ArrayList<>();
        for (Organization organization : organizations) {
            leaderBoards.addAll(showPaperLeaderBoardByOrganization(testPaperId, organization.getOrganizationId()));
        }
        leaderBoards.sort((o1, o2) -> Double.compare(o2.getScore(), o1.getScore()));
        // 存进缓存
        leaderBoardRedisDao.setLeaderBoard(key, leaderBoards);
        return leaderBoards;
    }

    private List<LeaderBoard> showLeaderBoardByOrganization(int organizationId) {
        String key = "showLeaderBoardByOrganization" + "_" + organizationId;
        List<LeaderBoard> redisLeaderBoards = (List<LeaderBoard>) leaderBoardRedisDao.getLeaderBoard(key);
        if (redisLeaderBoards != null) {
            return redisLeaderBoards;
        }
        List<User> users;
        users = userDao.findUserByOrganizationId(organizationId);
        List<TestPaper> testPapers = testPaperDao.findTestPaperByOrganizationIdAndTime(organizationId,
                Timestamp.valueOf(DateUtil.format(new Date())));
        List<Integer> testPaperIds = new ArrayList<>();

        for (TestPaper testPaper : testPapers) {
            testPaperIds.add(testPaper.getTestpaperId());
        }
        String organizationName = organizationDao.getById(organizationId).getOrganizationName();
        List<LeaderBoard> leaderBoards = new ArrayList<>();
        for (User user : users) {
            LeaderBoard leaderBoard = new LeaderBoard();
            leaderBoard.setImagePath(user.getImagePath());
            leaderBoard.setOrganizationName(organizationName);
            leaderBoard.setStudentId(user.getStudentId());
            leaderBoard.setUsername(user.getUserName());
            List<StudentTestResult> testResults = testDao.findStudentTestResultByUserIdAndTestPaperIds(user.getUserId(), testPaperIds);
            double scoreSum = 0.0;
            for (StudentTestResult testResult : testResults) {
                scoreSum += testResult.getSocre();
            }
            double avg = scoreSum / testPapers.size();
            DecimalFormat df = new DecimalFormat("#.00");
            leaderBoard.setScore(Double.parseDouble(df.format(avg)));
            leaderBoards.add(leaderBoard);
        }
        leaderBoards.sort((o1, o2) -> Double.compare(o2.getScore(), o1.getScore()));
        // 存进缓存
        leaderBoardRedisDao.setLeaderBoard(key, leaderBoards);
        return leaderBoards;
    }

    private List<LeaderBoard> showLeaderBoardByTeacher(int organizationId) {
        String key = "showLeaderBoardByTeacher" + "_" + organizationId;
        List<LeaderBoard> redisLeaderBoards = (List<LeaderBoard>) leaderBoardRedisDao.getLeaderBoard(key);
        if (redisLeaderBoards != null) {
            return redisLeaderBoards;
        }
        List<Organization> organizations = organizationDao.findOrganizationById(organizationId);
        List<LeaderBoard> leaderBoards = new ArrayList<>();
        for (Organization organization : organizations) {
            leaderBoards.addAll(showLeaderBoardByOrganization(organization.getOrganizationId()));
        }
        leaderBoards.sort((o1, o2) -> Double.compare(o2.getScore(), o1.getScore()));
        // 存进缓存
        leaderBoardRedisDao.setLeaderBoard(key, leaderBoards);
        return leaderBoards;
    }
}