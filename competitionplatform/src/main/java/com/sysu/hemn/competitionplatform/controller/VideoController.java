package com.sysu.hemn.competitionplatform.controller;

import com.alibaba.fastjson.JSONObject;
import com.sysu.hemn.competitionplatform.annotation.AdminToken;
import com.sysu.hemn.competitionplatform.annotation.CheckToken;
import com.sysu.hemn.competitionplatform.annotation.LoginToken;
import com.sysu.hemn.competitionplatform.entity.BrowseLog;
import com.sysu.hemn.competitionplatform.entity.Competition;
import com.sysu.hemn.competitionplatform.entity.Video;
import com.sysu.hemn.competitionplatform.entity.VideoEvaluation;
import com.sysu.hemn.competitionplatform.service.BrowseLogService;
import com.sysu.hemn.competitionplatform.service.VideoService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping("/competitionplatform/api/video")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VideoController {
    @Resource
    private VideoService videoService;
    @Resource
    private BrowseLogService browseLogService;

    @LoginToken
    @RequestMapping(value="/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String list(HttpServletRequest request) {
        Integer pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String title = request.getParameter("title");
        String category = convertCategory(request.getParameter("category"));
        String tags = request.getParameter("tags");
        if(tags != null && !tags.equals("")) {
            tags = tags.replaceAll(",", "|");
        }
        Long id = request.getParameter("id")==null ? null : Long.parseLong(request.getParameter("id"));
        String orderByClause = convertOrderByClause(request.getParameter("orderByClause"));
        List<Video> list = videoService.list(pageNumber, pageSize, category, title, tags, id, orderByClause);
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
        String category = convertCategory(request.getParameter("category"));
        String tags = request.getParameter("tags");
        if(tags != null && !tags.equals("")) {
            tags = tags.replaceAll(",", "|");
        }
        Long maxPage = videoService.maxPage(pageSize, category, title, tags);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("maxPage", maxPage);
        return result.toJSONString();
    }

    @LoginToken
    @RequestMapping(value="/find", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String findById(HttpServletRequest request) {
        Long id = Long.parseLong(request.getParameter("id"));
        Video video = videoService.findById(id);
        JSONObject result = new JSONObject();
        if (video == null) {
            result.put("code", -1);
            result.put("message", "视频不存在");
        } else {
            result.put("code", 1);
            BrowseLog browseLog = new BrowseLog();
            browseLog.setThemeType("视频");
            browseLog.setThemeId(id);
            browseLog.setUserId((Long) request.getAttribute("userId"));
            browseLogService.browse(browseLog);
            result.put("data", video);
        }
        return result.toJSONString();
    }

    @LoginToken
    @RequestMapping(value="/evaluations/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String listComments(HttpServletRequest request) {
        Integer pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        Long videoId = Long.parseLong(request.getParameter("videoId"));
        String orderByClause = convertEvaluationOrder(request.getParameter("orderByClause"));
        List<VideoEvaluation> list = videoService.listEvaluation(pageNumber, pageSize, videoId, orderByClause);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("list", list);
        return result.toJSONString();
    }

    @LoginToken
    @RequestMapping(value="/evaluations/maxpage", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String commentsMaxPage(HttpServletRequest request) {
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        Long videoId = Long.parseLong(request.getParameter("videoId"));
        Long maxPage = videoService.evaluationMaxPage(pageSize, videoId);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("maxPage", maxPage);
        return result.toJSONString();
    }

    @CheckToken
    @RequestMapping(value="/evaluations/release", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String releaseComment(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Long videoId = Long.parseLong(request.getParameter("videoId"));
        Integer score = Integer.parseInt(request.getParameter("score"));
        String comment = request.getParameter("comment");

        VideoEvaluation videoEvaluation = new VideoEvaluation();
        videoEvaluation.setUserId(userId);
        videoEvaluation.setVideoId(videoId);
        videoEvaluation.setScore(score);
        videoEvaluation.setComment(comment);
        Integer releaseRes = videoService.releaseEvaluation(videoEvaluation);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("status", releaseRes);
        return result.toJSONString();
    }

    @AdminToken
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String update(HttpServletRequest request) {
        Video video = defaultSetVideo(request);
        video.setId(Long.parseLong(request.getParameter("id")));
        videoService.update(video);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        return result.toJSONString();
    }

    @AdminToken
    @RequestMapping(value="/release", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String release(HttpServletRequest request) {
        Video video = defaultSetVideo(request);
        Long id = videoService.release(video);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("id", id);
        return result.toJSONString();
    }

    @AdminToken
    @RequestMapping(value="/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String delete(HttpServletRequest request) {
        Long id = Long.parseLong(request.getParameter("id"));
        videoService.delete(id);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        return result.toJSONString();
    }

    private static String convertCategory(String parm) {
        if (parm == null) return null;
        if (parm.equals("所有类别")) {
            return null;
        } else {
            return parm;
        }
    }

    private static String convertOrderByClause(String clause) {
        if (clause == null) return "release_time desc";
        if (clause.equals("按最热排序")) {
            return "view_number desc";
        } else if (clause.equals("按最新排序")) {
            return "release_time desc";
        } else if (clause.equals("按评分排序")) {
            return "score desc";
        } else {
            return "release_time desc";
        }
    }

    private static String convertEvaluationOrder(String clause) {
        if (clause == null) return "evaluate_time asc";
        if (clause.equals("按时间正序")) {
            return "evaluate_time asc";
        }  else {
            return "evaluate_time desc";
        }
    }

    private static Video defaultSetVideo(HttpServletRequest request) {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String introduction = request.getParameter("introduction");
        String figure = request.getParameter("figure");
        String category = request.getParameter("category");
        String tags = request.getParameter("tags");
        String src = request.getParameter("src");
        Video video = new Video();
        video.setTitle(title);
        video.setAuthor(author);
        video.setIntroduction(introduction);
        video.setFigure(figure);
        video.setCategory(category);
        video.setTags(tags);
        video.setSrc(src);
        return video;
    }
}
