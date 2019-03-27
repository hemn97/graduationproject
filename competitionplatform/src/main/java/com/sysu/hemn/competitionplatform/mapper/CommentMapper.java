package com.sysu.hemn.competitionplatform.mapper;

import com.sysu.hemn.competitionplatform.entity.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentMapper {
    @Select("<script>" +
            "select * from v_comments where live=1 " +
            "<when test='themeType != null and themeType != &apos;&apos;'>" +
            "AND theme_type = #{themeType}" +
            "</when>" +
            "<when test='themeId != null'>" +
            "AND theme_id = #{themeId}" +
            "</when>" +
            "<when test='fartherId != null and fartherId != -1'>" +
            "AND (farther_id = #{fartherId} or id=#{fartherId})" +
            "</when>" +
            "<when test='orderByClause != null and orderByClause != &apos;&apos;'>" +
            "order by ${orderByClause}" +
            "</when>" +
            "LIMIT #{offset},#{pageSize}" +
            "</script>")
    List<Comment> list(Integer offset, Integer pageSize, String themeType, Long themeId, Long fartherId, String orderByClause);

    @Select("<script>" +
            "select count(*) from v_comments where live=1 " +
            "<when test='themeType != null and themeType != &apos;&apos;'>" +
            "AND theme_type = #{themeType}" +
            "</when>" +
            "<when test='themeId != null'>" +
            "AND theme_id = #{themeId}" +
            "</when>" +
            "<when test='fartherId != null and fartherId != -1'>" +
            "AND (farther_id = #{fartherId} or id=#{fartherId})" +
            "</when>" +
            "</script>")
    Long count(String themeType, Long themeId, Long fartherId);

    @Insert("insert into t_comments(user_id, reply_id, theme_type, theme_id, farther_id, content, release_time, live) " +
            "value(#{userId}, #{replyId}, #{themeType}, #{themeId}, #{fartherId}, #{content}, now(), 1)")
    void insert(Comment comment);

}
