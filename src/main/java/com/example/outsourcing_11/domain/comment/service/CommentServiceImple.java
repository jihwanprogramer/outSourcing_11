package com.example.outsourcing_11.domain.comment.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.outsourcing_11.common.Status;
import com.example.outsourcing_11.common.exception.CustomException;
import com.example.outsourcing_11.domain.comment.dto.RequestCommentDto;
import com.example.outsourcing_11.domain.comment.dto.ResponseCommentDto;
import com.example.outsourcing_11.domain.comment.entity.Comment;
import com.example.outsourcing_11.domain.comment.repository.CommentRepository;
import com.example.outsourcing_11.domain.order.entity.Order;
import com.example.outsourcing_11.domain.order.entity.OrderStatus;
import com.example.outsourcing_11.domain.order.repository.OrderRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImple implements CommentService {

	private final OrderRepository orderRepository;
	private final CommentRepository commentRepository;

	public Order ValidateOrder(Long orderId, Long userId) {
		Order order = orderRepository.findByIdAndDeletedAtIsNull(orderId)
			.orElseThrow(() -> new CustomException("요청하신 주문은 존재하지 않거나 삭제되었습니다.", HttpStatus.BAD_REQUEST));
		if (!order.getUser().getId().equals(userId)) {
			throw new CustomException("리뷰 작성 또는 수정 권한이 없습니다.", HttpStatus.BAD_REQUEST);
		}
		if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
			throw new CustomException("리뷰는 배달 완료 이후에만 작성할 수 있습니다.", HttpStatus.BAD_REQUEST);
		}
		return order;
	}

	@Override
	public ResponseCommentDto createComment(Long orderId, Long userId, RequestCommentDto dto) {

		ValidateOrder(orderId, userId);
		Comment savecomment = new Comment(dto);
		commentRepository.save(savecomment);
		return new ResponseCommentDto(savecomment);
	}

	@Override
	public List<ResponseCommentDto> findCommentsByRatingRange(Long orderId, Long userId, int min, int max) {

		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Comment> comments = commentRepository.findByRatingBetweenAndDeletedAtIsNull(min, max, pageRequest);
		return comments.stream().map(ResponseCommentDto::new).toList();
	}

	public List<ResponseCommentDto> findByOrderComments(Long orderId, Long userId) {

		ValidateOrder(orderId, userId);

		return commentRepository.findWithRelationsByOrderId(orderId)
			.stream()
			.map(ResponseCommentDto::new)
			.toList();
	}

	@Override
	public ResponseCommentDto updateComment(Long orderId, Long userId, Long commentId, RequestCommentDto dto) {

		ValidateOrder(orderId, userId);

		Comment findcomment = commentRepository
			.findByIdAndOrderIdAndDeletedAtIsNull(commentId, orderId)
			.orElseThrow(() -> new CustomException("요청하신 리뷰를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
		findcomment.update(dto);
		commentRepository.save(findcomment);
		return new ResponseCommentDto(findcomment);
	}

	@Override
	public void deleteComment(Long orderId, Long userId, Long commentId) {

		ValidateOrder(orderId, userId);

		//softDelete 진행.
		Comment findcomment = commentRepository
			.findByIdAndOrderIdAndDeletedAtIsNull(commentId, orderId)
			.orElseThrow(() -> new CustomException("요청하신 리뷰를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
		Status status = Status.fromValue(true);
		findcomment.updateDeleteStatus(status.getValue());
		findcomment.timeWhenDeleted();
		commentRepository.save(findcomment);
	}
}
