package com.sysu.hemn.competitionplatform.service.impl;

import com.sysu.hemn.competitionplatform.entity.Message;
import com.sysu.hemn.competitionplatform.mapper.MessageMapper;
import com.sysu.hemn.competitionplatform.service.MessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;

    @Override
    public void send(Message message) {
        message.setStatus("未读");
        messageMapper.insert(message);
    }

    @Override
    public void read(Long id, Long recipientId) {
        messageMapper.update("已读", id, recipientId);
    }

    @Override
    public void unread(Long id) {
        messageMapper.update("未读", id, null);
    }

    @Override
    public List<Message> list(Integer pageNumber, Integer pageSize, Long senderId, Long recipientId, String status) {
        return messageMapper.list((pageNumber-1)*pageSize, pageSize, senderId, recipientId, status);
    }

    @Override
    public Long maxPage(Integer pageSize, Long senderId, Long recipientId, String status) {
        Long count = messageMapper.count(senderId, recipientId, status);
        return (count + pageSize - 1) / pageSize;
    }
}
