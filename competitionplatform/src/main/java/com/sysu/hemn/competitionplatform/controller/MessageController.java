package com.sysu.hemn.competitionplatform.controller;

import com.alibaba.fastjson.JSONObject;
import com.sysu.hemn.competitionplatform.annotation.CheckToken;
import com.sysu.hemn.competitionplatform.entity.Message;
import com.sysu.hemn.competitionplatform.service.MessageService;
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
@RequestMapping("/competitionplatform/api/message")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MessageController {
    @Resource
    private MessageService messageService;

    @CheckToken
    @RequestMapping(value="/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String list(HttpServletRequest request) {
        Integer pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String status = convertStatus(request.getParameter("status"));
        String mailbox = request.getParameter("mailbox");
        Long senderId = null;
        Long recipientId = null;
        if (mailbox.equals("收件箱")) {
            recipientId = (Long) request.getAttribute("userId");
        } else if (mailbox.equals("已发送私信")) {
            senderId = (Long) request.getAttribute("userId");
        }
        List<Message> list = messageService.list(pageNumber, pageSize, senderId, recipientId, status);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("list", list);
        return result.toJSONString();
    }

    @CheckToken
    @RequestMapping(value="/maxpage", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String maxPage(HttpServletRequest request) {
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String status = convertStatus(request.getParameter("status"));
        String mailbox = request.getParameter("mailbox");
        Long senderId = null;
        Long recipientId = null;
        if (mailbox.equals("收件箱")) {
            recipientId = (Long) request.getAttribute("userId");
        } else if (mailbox.equals("已发送私信")) {
            senderId = (Long) request.getAttribute("userId");
        }
        Long maxPage = messageService.maxPage(pageSize, senderId, recipientId, status);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("maxPage", maxPage);
        return result.toJSONString();
    }

    @CheckToken
    @RequestMapping(value="/read", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String read(HttpServletRequest request) {
        Long id = Long.parseLong(request.getParameter("id"));
        String all = request.getParameter("all");
        Long recipientId = null;
        if (all != null && all.equals("所有")) {
            recipientId = (Long) request.getAttribute("userId");
        }
        messageService.read(id, recipientId);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        return result.toJSONString();
    }

    @CheckToken
    @RequestMapping(value="/unread", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String unread(HttpServletRequest request) {
        Long id = Long.parseLong(request.getParameter("id"));
        messageService.unread(id);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        return result.toJSONString();
    }

    @CheckToken
    @RequestMapping(value="/send", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String send(HttpServletRequest request) {
        Long recipientId = Long.parseLong(request.getParameter("recipientId"));
        Long senderId = (Long) request.getAttribute("userId");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        Message message = new Message();
        message.setSenderId(senderId);
        message.setRecipientId(recipientId);
        message.setTitle(title);
        message.setContent(content);
        messageService.send(message);
        JSONObject result = new JSONObject();
        result.put("code", 1);
        return result.toJSONString();
    }

    private static String convertStatus(String status) {
        if (status == null) return null;
        if (status.equals("所有私信")) {
            return null;
        } else {
            return "未读";
        }
    }
}
