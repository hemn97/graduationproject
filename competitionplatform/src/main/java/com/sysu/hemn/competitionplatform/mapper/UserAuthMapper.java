package com.sysu.hemn.competitionplatform.mapper;

import com.sysu.hemn.competitionplatform.entity.UserAuth;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface UserAuthMapper {
    @Select("<script>" +
            "select * from t_user_auths where 1=1" +
            "<when test='identifier != null'>" +
            "AND identifier = #{identifier}" +
            "</when>" +
            "<when test='identityType != null'>" +
            "AND identity_type = #{identityType}" +
            "</when>" +
            "<when test='id != null'>" +
            "AND id = #{id}" +
            "</when>" +
            "</script>")
    UserAuth select(String identifier, String identityType, String id);

    @Insert("insert into t_user_auths(user_id,identity_type,identifier,credential) value(#{userId}, #{identityType}, " +
            "#{identifier}, #{credential})")
    void insert(UserAuth userAuth);
}
