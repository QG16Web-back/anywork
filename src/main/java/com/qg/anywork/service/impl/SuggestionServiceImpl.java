package com.qg.anywork.service.impl;

import com.qg.anywork.dao.SuggestionDao;
import com.qg.anywork.dto.RequestResult;
import com.qg.anywork.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create by ming on 18-8-5 下午10:22
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Service
public class SuggestionServiceImpl implements SuggestionService {

    @Autowired
    private SuggestionDao suggestionDao;

    @Override
    public RequestResult addSuggestion(int userId, String suggestion) {
        if (suggestion == null || suggestion.equals("")) return new RequestResult(0, "请填写你的建议");
        suggestionDao.addSuggestion(userId, suggestion);
        return new RequestResult(1, "成功");
    }
}
