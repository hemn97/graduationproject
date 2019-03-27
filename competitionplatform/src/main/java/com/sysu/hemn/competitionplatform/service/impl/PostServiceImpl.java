package com.sysu.hemn.competitionplatform.service.impl;

import com.sysu.hemn.competitionplatform.entity.BrowseLog;
import com.sysu.hemn.competitionplatform.entity.Comment;
import com.sysu.hemn.competitionplatform.entity.Post;
import com.sysu.hemn.competitionplatform.mapper.BrowseLogMapper;
import com.sysu.hemn.competitionplatform.mapper.CommentMapper;
import com.sysu.hemn.competitionplatform.mapper.PostMapper;
import com.sysu.hemn.competitionplatform.service.PostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("postService")
public class PostServiceImpl implements PostService {

    @Resource
    private PostMapper postMapper;
    @Resource
    private CommentMapper commentMapper;

    @Override
    public List<Post> list(Integer pageNumber, Integer pageSize, String category, String title, String orderByClause, Long userId) {
        return postMapper.list((pageNumber-1)*pageSize, pageSize, category, title, orderByClause, userId);
    }

    @Override
    public Long maxPage(Integer pageSize, String category, String title, Long userId) {
        Long count = postMapper.count(category, title, userId);
        return (count + pageSize - 1) / pageSize;
    }

    @Override
    public Post findById(Long id) {
        return postMapper.select(id);
    }

    @Override
    public List<Comment> listComments(Integer pageNumber, Integer pageSize, Long themeId, Long fartherId, String orderByClause) {
        return commentMapper.list((pageNumber-1)*pageSize, pageSize, "帖子", themeId, fartherId, orderByClause);
    }

    @Override
    public Long commentsMaxPage(Integer pageSize, Long themeId, Long fartherId) {
        Long count = commentMapper.count("帖子", themeId, fartherId);
        return (count + pageSize - 1) / pageSize;
    }

    @Override
    public void releaseComment(Comment comment) {
        commentMapper.insert(comment);
    }

    @Override
    public Long releasePost(Post post) {
        postMapper.insert(post);
        return post.getId();
    }

    @Override
    public void updatePost(Post post) {
        postMapper.update(post);
    }

    @Override
    public void deletePost(Long id) {
        postMapper.delete(id);
    }
}
