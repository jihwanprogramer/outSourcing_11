package com.example.outsourcing_11.domain.comment.service;

import java.util.List;

import com.example.outsourcing_11.domain.comment.dto.RequestCommentDto;
import com.example.outsourcing_11.domain.comment.dto.ResponseCommentDto;

public interface CommentService {
	ResponseCommentDto createComment(Long orderId, Long userId, RequestCommentDto dto);

	List<ResponseCommentDto> findCommentsByRatingRange(Long orderId, Long userId, int min, int max);

	List<ResponseCommentDto> findByOrderComments(Long orderId, Long userId);

	ResponseCommentDto updateComment(Long orderId, Long userId, Long commentId, RequestCommentDto dto);

	void deleteComment(Long orderId, Long userId, Long commentId);
}
