package com.sysu.hemn.competitionplatform.mapper;

import com.sysu.hemn.competitionplatform.entity.Team;
import com.sysu.hemn.competitionplatform.entity.Teammate;
import com.sysu.hemn.competitionplatform.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TeammateMapper {
    @Select("<script>" +
            "select * from v_teammates where 1=1 " +
            "<when test='teamId != null'>" +
            "AND team_id = #{teamId}" +
            "</when>" +
            "<when test='status != null'>" +
            "AND status = #{status}" +
            "</when>" +
            "<when test='nickname != null and nickname != &apos;&apos;'>" +
            "AND nickname like &apos;%${nickname}%&apos;" +
            "</when>" +
            "order by join_time asc " +
            "limit #{offset}, #{pageSize}" +
            "</script>")
    List<Teammate> list(Integer offset, Integer pageSize, Long teamId, String status, String nickname);

    @Select("<script>" +
            "select count(*) from v_teammates where 1=1 " +
            "<when test='teamId != null'>" +
            "AND team_id = #{teamId}" +
            "</when>" +
            "<when test='status != null'>" +
            "AND status = #{status}" +
            "</when>" +
            "<when test='nickname != null and nickname != &apos;&apos;'>" +
            "AND nickname like &apos;%${nickname}%&apos;" +
            "</when>" +
            "</script>")
    Long count(Long teamId, String status, String nickname);

    @Insert("insert into t_teammates(team_id, user_id, status, join_time) " +
            "value(#{teamId}, #{userId}, #{status}, now())")
    void insert(Long teamId, Long userId, String status);

    @Delete("<script>" +
            "delete from t_teammates where 1=1 " +
            "<when test='teamId != null'>" +
            "AND team_id = #{teamId}" +
            "</when>" +
            "<when test='userId != null'>" +
            "AND user_id = #{userId}" +
            "</when>" +
            "<when test='status != null'>" +
            "AND status = #{status}" +
            "</when>" +
            "</script>")
    void delete(Long teamId, Long userId, String status);

    @Select("<script>" +
            "select * from t_teammates where 1=1" +
            "<when test='teamId != null'>" +
            "AND team_id = #{teamId}" +
            "</when>" +
            "<when test='userId != null'>" +
            "AND user_id = #{userId}" +
            "</when>" +
            "<when test='status != null'>" +
            "AND status = #{status}" +
            "</when>" +
            "</script>")
    Teammate select(Long teamId, Long userId, String status);

    @Update("update t_teammates set status=#{status} where team_id=#{teamId} and user_id=#{userId}")
    void update(Long teamId, Long userId, String status);
}
