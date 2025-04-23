package com.example.outsourcing_11.domain.comment.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import com.example.outsourcing_11.domain.comment.dto.RequestCommentDto;
import com.example.outsourcing_11.domain.comment.dto.ResponseCommentDto;

public interface CommentService {
	ResponseCommentDto createComment(@RequestBody RequestCommentDto dto);

	List<ResponseCommentDto> findCommentsByRatingRange(int min, int max);

	ResponseCommentDto updateComment(Long commentid, @RequestBody RequestCommentDto dto);

	void deleteComment(Long id);
}
