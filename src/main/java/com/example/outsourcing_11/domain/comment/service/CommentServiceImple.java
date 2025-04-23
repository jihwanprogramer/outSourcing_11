package com.example.outsourcing_11.domain.comment.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import com.example.outsourcing_11.domain.comment.dto.RequestCommentDto;
import com.example.outsourcing_11.domain.comment.dto.ResponseCommentDto;
import com.example.outsourcing_11.domain.comment.entity.Comment;
import com.example.outsourcing_11.domain.comment.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImple implements CommentService {

	private static CommentRepository commentRepository;

	@Override
	public ResponseCommentDto createComment(RequestCommentDto dto) {
		//로그인 상태확인
		//주문상태를 확인하고 Compelete가 아닐경우 예외 발생하기.
		Comment savecomment = new Comment(dto);
		commentRepository.save(savecomment);
		return new ResponseCommentDto(savecomment);
	}

	@Override
	public List<ResponseCommentDto> findCommentsByRatingRange(int min, int max) {
		//로그인 상태 확인
		return commentRepository.findByRatingBetweenAndDeletedAtIsNull(min, max);
	}

	@Override
	public ResponseCommentDto updateComment(Long commentId, RequestCommentDto dto) {
		//본인 댓글이 아닌경우 수정 불가
		Comment findcomment = commentRepository.findByOrThrowElse(commentId);
		findcomment = new Comment(dto);
		return new ResponseCommentDto(findcomment);
	}

	@Override
	public void deleteComment(Long commentId) {
		//본인 댓글이 아닌 경우 삭제 불가
		//softDelete 진행.
		Comment findcomment = commentRepository.findByOrThrowElse(commentId);
		findcomment.updateDeleteStatus(1);
		findcomment.timeWhenDeleted();
	}
}
