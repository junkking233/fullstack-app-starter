package com.zhiqu.community.service;

import com.zhiqu.community.dto.CommentVO;
import com.zhiqu.community.dto.CreateCommentRequest;
import com.zhiqu.community.entity.Comment;

import java.util.List;

public interface CommentService {

    Comment createComment(CreateCommentRequest request, Long answerId, Long userId);

    List<CommentVO> getCommentsByAnswerId(Long answerId);

    void deleteComment(Long id, Long userId);
}
