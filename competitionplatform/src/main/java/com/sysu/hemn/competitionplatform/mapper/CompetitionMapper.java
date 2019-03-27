package com.sysu.hemn.competitionplatform.mapper;

import com.sysu.hemn.competitionplatform.entity.Competition;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CompetitionMapper {
    @Select("<script>" +
            "select * from v_competitions where live=1 " +
            "<when test='status != null and status != &apos;&apos;'>" +
            "AND status = #{status}" +
            "</when>" +
            "<when test='category != null and category != &apos;&apos;'>" +
            "AND category = #{category}" +
            "</when>" +
            "<when test='title != null and title != &apos;&apos;'>" +
            "AND title like &apos;%${title}%&apos;" +
            "</when>" +
            "<when test='orderByClause != null and orderByClause != &apos;&apos;'>" +
            "order by ${orderByClause}" +
            "</when>" +
            "LIMIT #{offset},#{pageSize}" +
            "</script>")
    List<Competition> list(Integer offset, Integer pageSize, String status, String category, String title, String orderByClause);

    @Select("<script>" +
            "select count(*) from v_competitions where live=1" +
            "<when test='status != null and status != &apos;&apos;'>" +
            "AND status = #{status}" +
            "</when>" +
            "<when test='category != null and category != &apos;&apos;'>" +
            "AND category = #{category}" +
            "</when>" +
            "<when test='title != null and title != &apos;&apos;'>" +
            "AND title like &apos;%${title}%&apos;" +
            "</when>" +
            "</script>")
    Long count(String status, String category, String title);

    @Select("<script>" +
            "select * from v_competitions where live=1" +
            "<when test='id != null'>" +
            "AND id = #{id}" +
            "</when>" +
            "</script>")
    Competition select(@Param("id") Long id);

    @Update("update t_competitions set live=0 where id=#{id}")
    void delete(@Param("id") Long id);

    @Update("update t_competitions set title=#{title},category=#{category},sponsor=#{sponsor},start_time=#{startTime}," +
            "end_time=#{endTime},status=#{status},tags=#{tags},details=#{details},link=#{link},logo=#{logo} where id=#{id}")
    void update(Competition competition);

    @Update("insert into t_competitions(title,category,sponsor,start_time,end_time,status,tags,details,link,heat,logo,live," +
            "register_time) value(#{title},#{category},#{sponsor},#{startTime},#{endTime},#{status},#{tags},#{details}," +
            "#{link},0,#{logo},1,now())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(Competition competition);
}
