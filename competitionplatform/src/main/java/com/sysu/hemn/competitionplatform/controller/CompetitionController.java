package com.sysu.hemn.competitionplatform.controller;

import com.alibaba.fastjson.JSONObject;
import com.sysu.hemn.competitionplatform.annotation.AdminToken;
import com.sysu.hemn.competitionplatform.annotation.CheckToken;
import com.sysu.hemn.competitionplatform.annotation.LoginToken;
import com.sysu.hemn.competitionplatform.entity.BrowseLog;
import com.sysu.hemn.competitionplatform.entity.Comment;
import com.sysu.hemn.competitionplatform.entity.Competition;
import com.sysu.hemn.competitionplatform.service.BrowseLogService;
import com.sysu.hemn.competitionplatform.service.CompetitionService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping("/competitionplatform/api/competition")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CompetitionController {
    @Resource
    private CompetitionService competitionService;
    @Resource
    private BrowseLogService browseLogService;

    @LoginToken
    @RequestMapping(value="/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String list(HttpServletRequest request) {
        Integer pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String title = request.getParameter("title");
        String status = convertCategory(request.getParameter("status"));
        String category = convertCategory(request.getParameter("category"));
        String orderByClause = convertOrderByClause(request.getParameter("orderByClause"));
        List<Competition> list = competitionService.list(pageNumber, pageSize, status, category, title, orderByClause);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("list", list);
        return result.toJSONString();
    }

    @LoginToken
    @RequestMapping(value="/maxpage", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String maxPage(HttpServletRequest request) {
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String title = request.getParameter("title");
        String status = convertCategory(request.getParameter("status"));
        String category = convertCategory(request.getParameter("category"));
        Long maxPage = competitionService.maxPage(pageSize, status, category, title);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("maxPage", maxPage);
        return result.toJSONString();
    }

    @LoginToken
    @RequestMapping(value="/find", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String findById(HttpServletRequest request) {
        Long id = Long.parseLong(request.getParameter("id"));
        Competition competition = competitionService.findById(id);
        JSONObject result = new JSONObject();
        if (competition == null) {
            result.put("code", -1);
            result.put("message", "竞赛不存在");
        } else {
            result.put("code", 1);
            BrowseLog browseLog = new BrowseLog();
            browseLog.setThemeType("竞赛");
            browseLog.setThemeId(id);
            browseLog.setUserId((Long) request.getAttribute("userId"));
            browseLogService.browse(browseLog);
            competition.setDetails(HtmlUtils.htmlUnescape(competition.getDetails()));
            result.put("data", competition);
        }
        return result.toJSONString();
    }

    @AdminToken
    @RequestMapping(value="/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String delete(HttpServletRequest request) {
        Long id = Long.parseLong(request.getParameter("id"));
        competitionService.delete(id);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        return result.toJSONString();
    }

    @LoginToken
    @RequestMapping(value="/comments/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String listComments(HttpServletRequest request) {
        Integer pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        Long themeId = Long.parseLong(request.getParameter("themeId"));
        Long fartherId = Long.parseLong(request.getParameter("fartherId"));
        String orderByClause = convertCommentOrder(request.getParameter("orderByClause"));
        List<Comment> list = competitionService.listComments(pageNumber, pageSize, themeId, fartherId, orderByClause);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("list", list);
        return result.toJSONString();
    }

    @LoginToken
    @RequestMapping(value="/comments/maxpage", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String commentsMaxPage(HttpServletRequest request) {
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        Long themeId = Long.parseLong(request.getParameter("themeId"));
        Long fartherId = Long.parseLong(request.getParameter("fartherId"));
        Long maxPage = competitionService.commentsMaxPage(pageSize, themeId, fartherId);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("maxPage", maxPage);
        return result.toJSONString();
    }

    @CheckToken
    @RequestMapping(value="/comments/release", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String releaseComment(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Long replyId = Long.parseLong(request.getParameter("replyId"));
        Long fartherId = Long.parseLong(request.getParameter("fartherId"));
        Long themeId = Long.parseLong(request.getParameter("themeId"));
        String content = request.getParameter("content");

        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setReplyId(replyId);
        comment.setFartherId(fartherId);
        comment.setThemeType("竞赛");
        comment.setThemeId(themeId);
        competitionService.releaseComment(comment);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        return result.toJSONString();
    }

    @AdminToken
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String update(HttpServletRequest request) {
        Competition competition = defaultSetCompetition(request);
        competition.setId(Long.parseLong(request.getParameter("id")));
        competitionService.update(competition);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        return result.toJSONString();
    }

    @AdminToken
    @RequestMapping(value="/release", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String release(HttpServletRequest request) {
        Competition competition = defaultSetCompetition(request);
        Long id = competitionService.release(competition);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("id", id);
        return result.toJSONString();
    }

    private static Competition defaultSetCompetition(HttpServletRequest request) {
        String title = request.getParameter("title");
        String sponsor = request.getParameter("sponsor");
        String category = request.getParameter("category");
        String tags = request.getParameter("tags");
        String status = request.getParameter("status");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String link = request.getParameter("link");
        String logo = request.getParameter("logo");
        String details = HtmlUtils.htmlEscape(request.getParameter("details"));
        Competition competition = new Competition();
        competition.setTitle(title);
        competition.setSponsor(sponsor);
        competition.setCategory(category);
        competition.setTags(tags);
        competition.setStatus(status);
        competition.setStartTime(startTime);
        competition.setEndTime(endTime);
        competition.setLink(link);
        competition.setLogo(logo);
        competition.setDetails(details);
        return competition;
    }

    private static String convertCategory(String parm) {
        if (parm == null) return null;
        if (parm.equals("所有状态")) {
            return null;
        } else if (parm.equals("所有类别")) {
            return null;
        } else {
            return parm;
        }
    }

    private static String convertOrderByClause(String clause) {
        if (clause == null) return "end_time desc";
        if (clause.equals("默认排序")) {
            return "end_time desc";
        } else if (clause.equals("按最热排序")) {
            return "heat desc";
        } else if (clause.equals("按最新排序")) {
            return "register_time desc";
        } else {
            return "end_time desc";
        }
    }

    private static String convertCommentOrder(String clause) {
        if (clause == null) return "release_time asc";
        if (clause.equals("按时间正序")) {
            return "release_time asc";
        } else {
            return "release_time desc";
        }
    }
}
