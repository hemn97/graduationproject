package com.sysu.hemn.competitionplatform.mapper;

import com.sysu.hemn.competitionplatform.entity.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface MessageMapper {
    @Insert("insert into t_messages(sender_id, recipient_id, title, content, status, send_time) " +
            "value(#{senderId}, #{recipientId}, #{title}, #{content}, #{status}, now())")
    void insert(Message message);

    @Update("<script>" +
            "update t_messages set status=#{status} where 1=1" +
            "<when test='id != null'>" +
            "AND id = #{id}" +
            "</when>" +
            "<when test='recipientId != null'>" +
            "AND recipient_id = #{recipientId}" +
            "</when>" +
            "</script>")
    void update(String status, Long id, Long recipientId);

    @Select("<script>" +
            "select * from v_messages where 1=1 " +
            "<when test='senderId != null'>" +
            "AND sender_id = #{senderId}" +
            "</when>" +
            "<when test='recipientId != null'>" +
            "AND recipient_id = #{recipientId}" +
            "</when>" +
            "<when test='status != null'>" +
            "AND status = #{status}" +
            "</when>" +
            "order by send_time desc " +
            "LIMIT #{offset},#{pageSize}" +
            "</script>")
    List<Message> list(Integer offset, Integer pageSize, Long senderId, Long recipientId, String status);

    @Select("<script>" +
            "select count(*) from v_messages where 1=1 " +
            "<when test='senderId != null'>" +
            "AND sender_id = #{senderId}" +
            "</when>" +
            "<when test='recipientId != null'>" +
            "AND recipient_id = #{recipientId}" +
            "</when>" +
            "<when test='status != null'>" +
            "AND status = #{status}" +
            "</when>" +
            "</script>")
    Long count(Long senderId, Long recipientId, String status);
}
