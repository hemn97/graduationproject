package com.sysu.hemn.competitionplatform.service;

import com.sysu.hemn.competitionplatform.entity.Comment;
import com.sysu.hemn.competitionplatform.entity.Post;

import java.util.List;

public interface PostService {
    List<Post> list(Integer pageNumber, Integer pageSize, String category, String title, String orderByClause, Long userId);
    Long maxPage(Integer pageSize, String category, String title, Long userId);
    Post findById(Long id);
    List<Comment> listComments(Integer pageNumber, Integer pageSize, Long themeId, Long fartherId, String orderByClause);
    Long commentsMaxPage(Integer pageSize, Long themeId, Long fartherId);
    void releaseComment(Comment comment);
    Long releasePost(Post post);
    void updatePost(Post post);
    void deletePost(Long id);
}
