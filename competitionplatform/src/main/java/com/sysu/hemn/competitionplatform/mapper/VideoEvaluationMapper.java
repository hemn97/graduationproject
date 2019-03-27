package com.sysu.hemn.competitionplatform.mapper;

import com.sysu.hemn.competitionplatform.entity.VideoEvaluation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface VideoEvaluationMapper {
    @Select("<script>" +
            "select * from v_video_evaluations where live=1 " +
            "<when test='videoId != null'>" +
            "AND video_id = #{videoId}" +
            "</when>" +
            "<when test='orderByClause != null and orderByClause != &apos;&apos;'>" +
            "order by ${orderByClause}" +
            "</when>" +
            "LIMIT #{offset},#{pageSize}" +
            "</script>")
    List<VideoEvaluation> list(Integer offset, Integer pageSize, Long videoId, String orderByClause);

    @Select("<script>" +
            "select count(*) from v_video_evaluations where live=1 " +
            "<when test='videoId != null'>" +
            "AND video_id = #{videoId}" +
            "</when>" +
            "</script>")
    Long count(@Param("videoId") Long videoId);

    @Insert("insert into t_video_evaluations(video_id, user_id, score, comment, live, evaluate_time) " +
            "value(#{videoId}, #{userId}, #{score}, #{comment}, 1, now())")
    void insert(VideoEvaluation videoEvaluation);

    @Select("<script>" +
            "select * from v_video_evaluations where live=1 " +
            "<when test='videoId != null'>" +
            "AND video_id = #{videoId}" +
            "</when>" +
            "<when test='userId != null'>" +
            "AND user_id = #{userId}" +
            "</when>" +
            "</script>")
    VideoEvaluation select(Long videoId, Long userId);
}
