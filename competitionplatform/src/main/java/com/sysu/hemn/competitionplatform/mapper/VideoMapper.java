package com.sysu.hemn.competitionplatform.mapper;

import com.sysu.hemn.competitionplatform.entity.Video;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface VideoMapper {
    @Select("<script>" +
            "select * from v_videos where live=1 " +
            "<when test='category != null and category != &apos;&apos;'>" +
            "AND category = #{category}" +
            "</when>" +
            "<when test='title != null and title != &apos;&apos;'>" +
            "AND title like &apos;%${title}%&apos;" +
            "</when>" +
            "<when test='tags != null and tags != &apos;&apos;'>" +
            "AND tags REGEXP #{tags}" +
            "</when>" +
            "<when test='id != null'>" +
            "AND id != #{id}" +
            "</when>" +
            "<when test='orderByClause != null and orderByClause != &apos;&apos;'>" +
            "order by ${orderByClause}" +
            "</when>" +
            "LIMIT #{offset},#{pageSize}" +
            "</script>")
    List<Video> list(Integer offset, Integer pageSize, String category, String title, String tags, Long id,
                     String orderByClause);

    @Select("<script>" +
            "select count(*) from v_videos where live=1" +
            "<when test='category != null and category != &apos;&apos;'>" +
            "AND category = #{category}" +
            "</when>" +
            "<when test='title != null and title != &apos;&apos;'>" +
            "AND title like &apos;%${title}%&apos;" +
            "</when>" +
            "<when test='tags != null and tags != &apos;&apos;'>" +
            "AND tags REGEXP #{tags}" +
            "</when>" +
            "</script>")
    Long count(String category, String title, String tags);

    @Select("<script>" +
            "select * from v_videos where live=1" +
            "<when test='id != null'>" +
            "AND id = #{id}" +
            "</when>" +
            "</script>")
    Video select(@Param("id") Long id);

    @Update("update t_videos set live=0 where id=#{id}")
    void delete(@Param("id") Long id);

    @Update("update t_videos set title=#{title},author=#{author},introduction=#{introduction},figure=#{figure}," +
            "category=#{category},tags=#{tags},src=#{src} where id=#{id}")
    void update(Video video);

    @Update("insert into t_videos(title,author,introduction,figure,category,tags,src,release_time,live) " +
            "value(#{title},#{author},#{introduction},#{figure},#{category},#{tags},#{src},now(),1)")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(Video video);
}
