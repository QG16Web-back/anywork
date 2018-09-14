package com.qg.anywork.service;

import com.qg.anywork.model.dto.RequestResult;

/**
 * Create by ming on 18-9-13 下午12:56
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
public interface LeaderBoardService {

    /**
     * 显示排行榜
     *
     * @param userId          用户ID
     * @param leaderBoardType 排行榜类型，1为在班级排，2是按老师教的班排
     * @return 排行榜
     */
    RequestResult showLeaderBoard(int userId, int leaderBoardType);

    /**
     * 显示每个试卷的排行榜
     *
     * @param userId          用户ID
     * @param leaderBoardType 排行榜类型，1为在班级排，2是按老师教的班排
     * @param testPaperId     试卷ID
     * @return 试卷排行榜
     */
    RequestResult showPaperLeaderBoard(int userId, int leaderBoardType, int testPaperId);
}
