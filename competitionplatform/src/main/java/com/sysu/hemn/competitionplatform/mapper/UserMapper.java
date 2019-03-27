package com.sysu.hemn.competitionplatform.mapper;

import com.sysu.hemn.competitionplatform.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {
    @Select("<script>" +
            "select * from v_users where 1=1" +
            "<when test='id != null'>" +
            "AND id = #{id}" +
            "</when>" +
            "<when test='nickname != null'>" +
            "AND nickname = #{nickname}" +
            "</when>" +
            "<when test='username != null'>" +
            "AND username = #{username}" +
            "</when>" +
            "<when test='email != null'>" +
            "AND email = #{email}" +
            "</when>" +
            "</script>")
    User select(Long id, String nickname, String username, String email);

    @Update("update t_users set school=#{school},tel_number=#{telNumber},gender=#{gender},role_type=#{roleType}, " +
            "skills=#{skills},activate_code=#{activateCode} where id=#{id}")
    void update(User user);

    @Insert("insert into t_users(nickname,gender,realname,tel_number,skills,school,activate_code," +
            "register_ip,register_time,role_type) value(#{nickname},#{gender},#{realname},#{telNumber},#{skills},#{school}," +
            "#{activateCode},#{registerIp},now(),#{roleType})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(User user);

    @Select("<script>" +
            "select * from v_users where 1=1 " +
            "<when test='searchKey != null and searchKey != &apos;&apos;'>" +
            "AND (nickname like &apos;%${searchKey}%&apos; or username like &apos;%${searchKey}%&apos; " +
            "or email like &apos;%${searchKey}%&apos;)" +
            "</when>" +
            "<when test='roleType != null and roleType != &apos;&apos; or 0 == roleType'>" +
            "AND role_type=#{roleType}" +
            "</when>" +
            "<when test='orderByClause != null and orderByClause != &apos;&apos;'>" +
            "order by ${orderByClause}" +
            "</when>" +
            "LIMIT #{offset},#{pageSize}" +
            "</script>")
    List<User> list(Integer offset, Integer pageSize, String searchKey, Integer roleType, String orderByClause);

    @Select("<script>" +
            "select count(*) from v_users where 1=1 " +
            "<when test='searchKey != null and searchKey != &apos;&apos;'>" +
            "AND (nickname like &apos;%${searchKey}%&apos; or username like &apos;%${searchKey}%&apos; " +
            "or email like &apos;%${searchKey}%&apos;)" +
            "</when>" +
            "<when test='roleType != null and roleType != &apos;&apos; or 0 == roleType'>" +
            "AND role_type=#{roleType}" +
            "</when>" +
            "</script>")
    Long count(String searchKey, Integer roleType);
}
