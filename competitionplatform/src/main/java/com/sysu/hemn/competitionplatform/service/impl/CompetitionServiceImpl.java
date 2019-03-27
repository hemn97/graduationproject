package com.sysu.hemn.competitionplatform.service.impl;

import com.sysu.hemn.competitionplatform.entity.Comment;
import com.sysu.hemn.competitionplatform.entity.Competition;
import com.sysu.hemn.competitionplatform.mapper.CommentMapper;
import com.sysu.hemn.competitionplatform.mapper.CompetitionMapper;
import com.sysu.hemn.competitionplatform.service.CompetitionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("competitionService")
public class CompetitionServiceImpl implements CompetitionService {

    @Resource
    private CompetitionMapper competitionMapper;
    @Resource
    private CommentMapper commentMapper;

    @Override
    public List<Competition> list(Integer pageNumber, Integer pageSize, String status, String category, String title, String orderByClause) {
        return competitionMapper.list((pageNumber-1)*pageSize, pageSize, status, category, title, orderByClause);
    }

    @Override
    public Long maxPage(Integer pageSize, String status, String category, String title) {
        Long count = competitionMapper.count(status, category, title);
        return (count + pageSize - 1) / pageSize;
    }

    @Override
    public Competition findById(Long id) {
        return competitionMapper.select(id);
    }

    @Override
    public List<Comment> listComments(Integer pageNumber, Integer pageSize, Long themeId, Long fartherId, String orderByClause) {
        return commentMapper.list((pageNumber-1)*pageSize, pageSize, "竞赛", themeId, fartherId, orderByClause);
    }

    @Override
    public Long commentsMaxPage(Integer pageSize, Long themeId, Long fartherId) {
        Long count = commentMapper.count("竞赛", themeId, fartherId);
        return (count + pageSize - 1) / pageSize;
    }

    @Override
    public void releaseComment(Comment comment) {
        commentMapper.insert(comment);
    }

    @Override
    public void delete(Long id) {
        competitionMapper.delete(id);
    }

    @Override
    public void update(Competition competition) {
        competitionMapper.update(competition);
    }

    @Override
    public Long release(Competition competition) {
        competitionMapper.insert(competition);
        return competition.getId();
    }
}
