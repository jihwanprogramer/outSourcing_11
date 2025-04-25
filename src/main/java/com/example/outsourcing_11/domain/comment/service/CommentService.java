package com.example.outsourcing_11.domain.comment.service;

import java.util.List;

import com.example.outsourcing_11.domain.comment.dto.user.RequestCommentDto;
import com.example.outsourcing_11.domain.comment.dto.user.ResponseCommentDto;

public interface CommentService {
	ResponseCommentDto createComment(Long orderId, Long userId, RequestCommentDto dto);

	List<ResponseCommentDto> findCommentsByRatingRange(Long orderId, Long userId, int min, int max);

	ResponseCommentDto updateComment(Long orderId, Long userId, Long commentId, RequestCommentDto dto);

	void deleteComment(Long orderId, Long userId, Long commentId);
}
