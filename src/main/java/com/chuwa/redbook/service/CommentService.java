package com.chuwa.redbook.service;

import com.chuwa.redbook.payload.CommentDto;

import java.util.List;

/**
 * @author b1go
 * @date 6/23/22 11:13 PM
 */
public interface CommentService {

    CommentDto createComment(long postId, CommentDto commentDto);

    List<CommentDto> getCommentsByPostId(long postId);

    CommentDto getCommentById(Long postId, Long commentId);

    CommentDto updateComment(Long postId, Long commentId, CommentDto commentDtoRequest);

    void deleteComment(Long postId, Long commentId);

}
