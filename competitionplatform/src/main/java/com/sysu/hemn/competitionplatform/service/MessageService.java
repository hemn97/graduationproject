package com.sysu.hemn.competitionplatform.service;

import com.sysu.hemn.competitionplatform.entity.Message;

import java.util.List;

public interface MessageService {
    void send(Message message);
    void read(Long id, Long recipientId);
    void unread(Long id);
    List<Message> list(Integer pageNumber, Integer pageSize, Long senderId, Long recipientId, String status);
    Long maxPage(Integer pageSize, Long senderId, Long recipientId, String status);
}
