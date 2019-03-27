package com.sysu.hemn.competitionplatform.service;

import com.sysu.hemn.competitionplatform.entity.Video;
import com.sysu.hemn.competitionplatform.entity.VideoEvaluation;

import java.util.List;

public interface VideoService {
    List<Video> list(Integer pageNumber, Integer pageSize, String category, String title, String tags, Long id,
                     String orderByClause);
    Long maxPage(Integer pageSize, String category, String title, String tags);
    Video findById(Long id);
    List<VideoEvaluation> listEvaluation(Integer pageNumber, Integer pageSize, Long videoId, String orderByClause);
    Long evaluationMaxPage(Integer pageSize, Long videoId);
    Integer releaseEvaluation(VideoEvaluation videoEvaluation);
    void delete(Long id);
    void update(Video video);
    Long release(Video video);
}
