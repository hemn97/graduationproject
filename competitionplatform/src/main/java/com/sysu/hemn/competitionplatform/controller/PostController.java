package com.sysu.hemn.competitionplatform.controller;

import com.alibaba.fastjson.JSONObject;
import com.sysu.hemn.competitionplatform.annotation.AdminToken;
import com.sysu.hemn.competitionplatform.annotation.CheckToken;
import com.sysu.hemn.competitionplatform.annotation.LoginToken;
import com.sysu.hemn.competitionplatform.entity.BrowseLog;
import com.sysu.hemn.competitionplatform.entity.Comment;
import com.sysu.hemn.competitionplatform.entity.Post;
import com.sysu.hemn.competitionplatform.entity.Video;
import com.sysu.hemn.competitionplatform.service.BrowseLogService;
import com.sysu.hemn.competitionplatform.service.PostService;
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
@RequestMapping("/competitionplatform/api/post")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PostController {

    @Resource
    private PostService postService;
    @Resource
    private BrowseLogService browseLogService;

    @LoginToken
    @RequestMapping(value="/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String list(HttpServletRequest request) {
        Integer pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String title = request.getParameter("title");
        String category = convertParameter(request.getParameter("category"));
        String orderByClause = convertOrderByClause(request.getParameter("orderByClause"));
        List<Post> list = postService.list(pageNumber, pageSize, category, title, orderByClause, null);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("list", list);
        return result.toJSONString();
    }


    @CheckToken
    @RequestMapping(value="/list/my", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String listMy(HttpServletRequest request) {
        Integer pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String title = request.getParameter("title");
        String category = convertParameter(request.getParameter("category"));
        String orderByClause = convertOrderByClause(request.getParameter("orderByClause"));
        Long userId = (Long) request.getAttribute("userId");
        List<Post> list = postService.list(pageNumber, pageSize, category, title, orderByClause, userId);
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
        String category = convertParameter(request.getParameter("category"));
        Long maxPage = postService.maxPage(pageSize, category, title, null);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("maxPage", maxPage);
        return result.toJSONString();
    }

    @CheckToken
    @RequestMapping(value="/maxpage/my", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String maxPageMy(HttpServletRequest request) {
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String title = request.getParameter("title");
        String category = convertParameter(request.getParameter("category"));
        Long userId = (Long) request.getAttribute("userId");
        Long maxPage = postService.maxPage(pageSize, category, title, userId);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("maxPage", maxPage);
        return result.toJSONString();
    }

    @LoginToken
    @RequestMapping(value="/find", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String findById(HttpServletRequest request) {
        Long id = Long.parseLong(request.getParameter("id"));
        Post post = postService.findById(id);
        post.setContent(HtmlUtils.htmlUnescape(post.getContent()));
        JSONObject result = new JSONObject();
        if (post == null) {
            result.put("code", -1);
            result.put("message", "帖子不存在");
        } else {
            result.put("code", 1);
            BrowseLog browseLog = new BrowseLog();
            browseLog.setThemeType("帖子");
            browseLog.setThemeId(id);
            browseLog.setUserId((Long) request.getAttribute("userId"));
            browseLogService.browse(browseLog);
            result.put("data", post);
        }
        return result.toJSONString();
    }

    @CheckToken
    @RequestMapping(value="/release", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String releasePost(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        Post post = defaultSetPost(request);
        post.setUserId(userId);
        Long id = postService.releasePost(post);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("id", id);
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
        List<Comment> list = postService.listComments(pageNumber, pageSize, themeId, fartherId, orderByClause);
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
        Long maxPage = postService.commentsMaxPage(pageSize, themeId, fartherId);
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
        comment.setThemeType("帖子");
        comment.setThemeId(themeId);
        postService.releaseComment(comment);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        return result.toJSONString();
    }

    @AdminToken
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String update(HttpServletRequest request) {
        Post post = defaultSetPost(request);
        post.setId(Long.parseLong(request.getParameter("id")));
        postService.updatePost(post);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        return result.toJSONString();
    }

    @AdminToken
    @RequestMapping(value="/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String delete(HttpServletRequest request) {
        Long id = Long.parseLong(request.getParameter("id"));
        postService.deletePost(id);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        return result.toJSONString();
    }

    private static String convertParameter(String param) {
        if (param == null) return null;
        if (param.equals("首页")) {
            return null;
        } else {
            return param;
        }
    }

    private static String convertOrderByClause(String clause) {
        if (clause == null) return "release_time desc";
        if (clause.equals("按时间排序")) {
            return "release_time desc";
        } else if (clause.equals("按浏览数排序")) {
            return "view_number desc";
        } else if (clause.equals("按评论数排序")) {
            return "comment_number desc";
        } else {
            return "release_time desc";
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

    private static Post defaultSetPost(HttpServletRequest request) {
        String title = request.getParameter("title");
        String content = HtmlUtils.htmlEscape(request.getParameter("content"));
        String abstractContent = request.getParameter("abstractContent");
        String category = request.getParameter("category");

        Post post = new Post();
        post.setTitle(title);
        post.setCategory(category);
        post.setContent(content);
        post.setAbstractContent(abstractContent);
        return post;
    }
}
