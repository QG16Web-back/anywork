package com.qg.anywork.web;

import com.qg.anywork.enums.StatEnum;
import com.qg.anywork.exception.common.ParamNotExistException;
import com.qg.anywork.model.dto.RequestResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * Create by ming on 18-9-12 下午2:16
 * <p>
 * 排行榜
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
public class LeaderBoardController {

    /**
     * 显示总的排行榜
     */
    @PostMapping("/leaderboard/show")
    public RequestResult showLeaderBoard(@RequestBody Map<String, Integer> map) {
        if (!map.containsKey("leaderboardType")) {
            throw new ParamNotExistException(StatEnum.PARAM_IS_NOT_EXIST);
        }
        int leaderboardType = map.get("leaderboardType");

        return null;
    }

    /**
     * 显示每个测试的排行榜
     */
    @PostMapping("leaderboard/paper/show")
    public RequestResult showPaperLeaderBoard() {
        return null;
    }
}
