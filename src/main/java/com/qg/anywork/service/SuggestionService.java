package com.qg.anywork.service;

import com.qg.anywork.dto.RequestResult;


/**
 * Created by logan on 2017/8/18.
 */
public interface SuggestionService {
    RequestResult addSuggestion(int userId, String suggestion);
}
