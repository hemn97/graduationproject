package com.sysu.hemn.competitionplatform.service;

import com.sysu.hemn.competitionplatform.entity.Comment;
import com.sysu.hemn.competitionplatform.entity.Competition;

import java.util.List;

public interface CompetitionService {
    List<Competition> list(Integer pageNumber, Integer pageSize, String status, String category, String title, String orderByClause);
    Long maxPage(Integer pageSize, String status, String category, String title);
    Competition findById(Long id);
    List<Comment> listComments(Integer pageNumber, Integer pageSize, Long themeId, Long fartherId, String orderByClause);
    Long commentsMaxPage(Integer pageSize, Long themeId, Long fartherId);
    void releaseComment(Comment comment);
    void delete(Long id);
    void update(Competition competition);
    Long release(Competition competition);
}
