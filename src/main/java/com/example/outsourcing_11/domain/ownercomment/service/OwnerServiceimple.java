package com.example.outsourcing_11.domain.ownercomment.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.outsourcing_11.common.Status;
import com.example.outsourcing_11.common.exception.CustomException;
import com.example.outsourcing_11.domain.comment.entity.Comment;
import com.example.outsourcing_11.domain.comment.repository.CommentRepository;
import com.example.outsourcing_11.domain.ownercomment.dto.OwnerRequestCommentDto;
import com.example.outsourcing_11.domain.ownercomment.dto.OwnerResponseCommentDto;
import com.example.outsourcing_11.domain.ownercomment.entity.OwnerComment;
import com.example.outsourcing_11.domain.ownercomment.repository.OwnerCommentRepository;

@Service
@RequiredArgsConstructor
public class OwnerServiceimple implements OwnerService {

	private final CommentRepository commentRepository;
	private final OwnerCommentRepository ownerCommentRepository;

	@Override
	public OwnerResponseCommentDto createOwerComment(Long commentId, OwnerRequestCommentDto dto) {
		Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
			.orElseThrow(() -> new CustomException("리뷰가 존재 하지 않습니다.", HttpStatus.NOT_FOUND));
		OwnerComment ownerComment = new OwnerComment(dto);

		ownerComment.updateComment(comment);
		comment.updateOwenrComment(ownerComment);

		ownerCommentRepository.save(ownerComment);
		return new OwnerResponseCommentDto(ownerComment);
	}

	@Override
	public List<OwnerResponseCommentDto> getOwnerComment(Long storeId, Long commentId) {

		// Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
		// 	.orElseThrow(() -> new CustomException("리뷰가 존재 하지 않습니다.", HttpStatus.NOT_FOUND));

		Optional<OwnerComment> ownerComments = ownerCommentRepository.findByStoreIdAndCommentId(storeId, commentId);

		return ownerComments.stream().map(OwnerResponseCommentDto::new).toList();
	}

	/**
	 *  사장님 대댓글 전체 조회
	 * @param storeId
	 * @param commentId
	 * @return
	 */
	@Override
	public List<OwnerResponseCommentDto> getOwnerComments(Long storeId, Long commentId) {
		Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
			.orElseThrow(() -> new CustomException("리뷰가 존재 하지 않습니다.", HttpStatus.NOT_FOUND));

		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<OwnerComment> ownerComments = ownerCommentRepository.findByStoreIdAndDeletedFalse(storeId, pageRequest);

		return ownerComments.stream().map(OwnerResponseCommentDto::new).toList();
	}

	@Override
	public OwnerResponseCommentDto updateOwnerComment(Long commentId, OwnerRequestCommentDto dto) {
		Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
			.orElseThrow(() -> new CustomException("리뷰가 존재 하지 않습니다.", HttpStatus.NOT_FOUND));

		if (comment.getOwnerComment() == null) {
			throw new CustomException("사장님 댓글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
		}
		OwnerComment ownerComment = comment.getOwnerComment();
		ownerComment.updateContent(dto.getContent());

		ownerCommentRepository.save(ownerComment);
		return new OwnerResponseCommentDto(ownerComment);
	}

	@Override
	public void deleteOwerComment(Long commentId) {
		Comment comment = commentRepository.findByIdAndIsDeletedFalse(commentId)
			.orElseThrow(() -> new CustomException("리뷰가 존재 하지 않습니다.", HttpStatus.NOT_FOUND));
		if (comment.getOwnerComment() == null) {
			throw new CustomException("사장님 댓글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
		}

		OwnerComment findOwnerComment = comment.getOwnerComment();
		Status status = Status.fromValue(true);
		findOwnerComment.updateDeleteStatus(status.getValue());
		findOwnerComment.timeWhenDeleted();
	}
}
