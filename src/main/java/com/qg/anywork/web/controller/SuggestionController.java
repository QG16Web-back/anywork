package com.qg.anywork.web.controller;

import com.qg.anywork.enums.StatEnum;
import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.model.po.Suggestion;
import com.qg.anywork.model.po.User;
import com.qg.anywork.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public RequestResult addSuggestion(@RequestBody Map map, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        String description = (String) map.get("description");
        Suggestion suggestion = new Suggestion();
        suggestion.setUser(user);
        suggestion.setDescription(description);
        return suggestionService.addSuggestion(suggestion);
    }

    @PostMapping("/show")
    public RequestResult showSuggestion(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user.getMark() == 0) {
            return new RequestResult(StatEnum.NOT_HAVE_POWER);
        }
        return suggestionService.show();
    }
}
