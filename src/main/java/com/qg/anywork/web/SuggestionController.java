package com.qg.anywork.web;

import com.qg.anywork.dto.RequestResult;
import com.qg.anywork.model.User;
import com.qg.anywork.service.SuggestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author logan
 * @date 2017/8/18
 */
@RestController
@RequestMapping("/suggest")
public class SuggestionController {
    @Autowired
    private SuggestionService suggestionService;

    private static final Logger logger = LoggerFactory.getLogger(SuggestionController.class);

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public RequestResult addSuggestion(@RequestBody Map map, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
//        User user = new User(); user.setUserId(1);
        String suggestion = (String) map.get("suggestion");
        return suggestionService.addSuggestion(user.getUserId(), suggestion);
    }
}
