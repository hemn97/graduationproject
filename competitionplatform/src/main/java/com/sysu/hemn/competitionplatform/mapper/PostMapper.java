package com.sysu.hemn.competitionplatform.mapper;

import com.sysu.hemn.competitionplatform.entity.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PostMapper {
    @Select("<script>" +
            "select * from v_posts where live=1 " +
            "<when test='category != null and category != &apos;&apos;'>" +
            "AND category = #{category}" +
            "</when>" +
            "<when test='userId != null'>" +
            "AND user_id = #{userId}" +
            "</when>" +
            "<when test='title != null and title != &apos;&apos;'>" +
            "AND title like &apos;%${title}%&apos;" +
            "</when>" +
            "<when test='orderByClause != null and orderByClause != &apos;&apos;'>" +
            "order by ${orderByClause}" +
            "</when>" +
            "LIMIT #{offset},#{pageSize}" +
            "</script>")
    List<Post> list(Integer offset, Integer pageSize, String category, String title, String orderByClause, Long userId);

    @Select("<script>" +
            "select count(*) from v_posts where live=1" +
            "<when test='category != null and category != &apos;&apos;'>" +
            "AND category = #{category}" +
            "</when>" +
            "<when test='userId != null'>" +
            "AND user_id = #{userId}" +
            "</when>" +
            "<when test='title != null and title != &apos;&apos;'>" +
            "AND title like &apos;%${title}%&apos;" +
            "</when>" +
            "</script>")
    Long count(String category, String title, Long userId);

    @Select("<script>" +
            "select * from v_posts where live=1" +
            "<when test='id != null'>" +
            "AND id = #{id}" +
            "</when>" +
            "</script>")
    Post select(@Param("id") Long id);

    @Insert("insert into t_posts(title, user_id, content, abstract_content, category, release_time, live) " +
            "value(#{title}, #{userId}, #{content}, #{abstractContent}, #{category}, now(), 1)")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(Post post);

    @Update("update t_posts set live=0 where id=#{id}")
    void delete(@Param("id") Long id);

    @Update("update t_posts set title=#{title},content=#{content},abstract_content=#{abstractContent}," +
            "category=#{category} where id=#{id}")
    void update(Post post);
}
