package com.sysu.hemn.competitionplatform.mapper;

import com.sysu.hemn.competitionplatform.entity.Team;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TeamMapper {
    @Select("<script>" +
            "select * from v_myteam where 1=1" +
            "<when test='userId != null'>" +
            "AND user_id = #{userId}" +
            "</when>" +
            "<when test='comptId != null'>" +
            "AND compt_id = #{comptId}" +
            "</when>" +
            "<when test='teammateStatus != null'>" +
            "AND status = #{teammateStatus}" +
            "</when>" +
            "</script>")
    @Results({
            @Result(property = "status", column = "team_status"),
            @Result(property = "id", column = "team_id"),

    })
    Team selectMyTeam(Long userId, Long comptId, String teammateStatus);

    @Select("<script>" +
            "select * from v_myteam where 1=1 " +
            "<when test='userId != null'>" +
            "AND user_id = #{userId}" +
            "</when>" +
            "<when test='status != null'>" +
            "AND status = #{status}" +
            "</when>" +
            "<when test='name != null and name != &apos;&apos;'>" +
            "AND name like &apos;%${name}%&apos;" +
            "</when>" +
            "order by join_time desc " +
            "LIMIT #{offset},#{pageSize}" +
            "</script>")
    List<Team> listMyTeams(Integer offset, Integer pageSize, Long userId, String status, String name);

    @Select("<script>" +
            "select * from t_teams where 1=1" +
            "<when test='id != null'>" +
            "AND id = #{id}" +
            "</when>" +
            "</script>")
    Team select(@Param("id") Long id);

    @Select("<script>" +
            "select * from v_teams where 1=1 " +
            "<when test='comptId != null'>" +
            "AND compt_id = #{comptId}" +
            "</when>" +
            "<when test='status != null'>" +
            "AND status = #{status}" +
            "</when>" +
            "<when test='captainId != null'>" +
            "AND captainId = #{captainId}" +
            "</when>" +
            "<when test='name != null and name != &apos;&apos;'>" +
            "AND name like &apos;%${name}%&apos;" +
            "</when>" +
            "order by register_time asc " +
            "LIMIT #{offset},#{pageSize}" +
            "</script>")
    List<Team> listTeam(Integer offset, Integer pageSize, Long comptId, String status, Long captainId, String name);

    @Select("<script>" +
            "select count(*) from v_teams where 1=1 " +
            "<when test='comptId != null'>" +
            "AND compt_id = #{comptId}" +
            "</when>" +
            "<when test='status != null'>" +
            "AND status = #{status}" +
            "</when>" +
            "<when test='captainId != null'>" +
            "AND captainId = #{captainId}" +
            "</when>" +
            "<when test='name != null and name != &apos;&apos;'>" +
            "AND name like &apos;%${name}%&apos;" +
            "</when>" +
            "</script>")
    Long count(Long comptId, String status, Long captainId, String name);

    @Select("<script>" +
            "select count(*) from v_myteam where 1=1 " +
            "<when test='userId != null'>" +
            "AND user_id = #{userId}" +
            "</when>" +
            "<when test='status != null'>" +
            "AND status = #{status}" +
            "</when>" +
            "<when test='name != null and name != &apos;&apos;'>" +
            "AND name like &apos;%${name}%&apos;" +
            "</when>" +
            "</script>")
    Long countMy(Long userId, String status, String name);

    @Insert("insert into t_teams(compt_id, name, captain_id, motto, status, register_time) " +
            "value(#{comptId}, #{name}, #{captainId}, #{motto}, #{status}, now())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(Team team);

    @Delete("delete from t_teams where id=#{id}")
    void delete(@Param("id") Long id);

    @Update("update t_teams set name=#{name},motto=#{motto},status=#{status} where id=#{id}")
    void update(Team team);
}
