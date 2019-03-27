package com.sysu.hemn.competitionplatform.service.impl;

import com.sysu.hemn.competitionplatform.entity.Video;
import com.sysu.hemn.competitionplatform.entity.VideoEvaluation;
import com.sysu.hemn.competitionplatform.mapper.VideoEvaluationMapper;
import com.sysu.hemn.competitionplatform.mapper.VideoMapper;
import com.sysu.hemn.competitionplatform.service.VideoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("videoService")
public class VideoServiceImpl implements VideoService {
    @Resource
    private VideoMapper videoMapper;
    @Resource
    private VideoEvaluationMapper videoEvaluationMapper;

    @Override
    public List<Video> list(Integer pageNumber, Integer pageSize, String category, String title, String tags,
                            Long id, String orderByClause) {
        return videoMapper.list((pageNumber-1)*pageSize, pageSize, category, title, tags, id, orderByClause);
    }

    @Override
    public Long maxPage(Integer pageSize, String category, String title, String tags) {
        Long count = videoMapper.count(category, title, tags);
        return (count + pageSize - 1) / pageSize;
    }

    @Override
    public Video findById(Long id) {
        return videoMapper.select(id);
    }

    @Override
    public List<VideoEvaluation> listEvaluation(Integer pageNumber, Integer pageSize, Long videoId, String orderByClause) {
        return videoEvaluationMapper.list((pageNumber-1)*pageSize, pageSize, videoId, orderByClause);
    }

    @Override
    public Long evaluationMaxPage(Integer pageSize, Long videoId) {
        Long count = videoEvaluationMapper.count(videoId);
        return (count + pageSize - 1) / pageSize;
    }

    @Override
    public Integer releaseEvaluation(VideoEvaluation videoEvaluation) {
        VideoEvaluation hasRelease = videoEvaluationMapper.select(videoEvaluation.getVideoId(), videoEvaluation.getUserId());
        if (hasRelease != null) {
            return -1;
        } else {
            videoEvaluationMapper.insert(videoEvaluation);
            return 1;
        }
    }

    @Override
    public void delete(Long id) {
        videoMapper.delete(id);
    }

    @Override
    public void update(Video video) {
        videoMapper.update(video);
    }

    @Override
    public Long release(Video video) {
        videoMapper.insert(video);
        return video.getId();
    }
}
