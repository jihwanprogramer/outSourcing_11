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

	private static CommentRepository commentRepository;
	private static OrderRepository orderRepository;

	@Override
	public ResponseCommentDto createComment(Long orderId, Long userId, RequestCommentDto dto) {

		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException("존재하지 않는 주문입니다.", HttpStatus.NOT_FOUND));
		if (!order.getUser().equals(userId)) {
			throw new CustomException("작성 권한이 없습니다.", HttpStatus.BAD_REQUEST);
		}
		if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
			throw new CustomException("배달완료가되지 않았습니다.", HttpStatus.BAD_REQUEST);
		}

		Comment savecomment = new Comment(dto);
		commentRepository.save(savecomment);
		return new ResponseCommentDto(savecomment);
	}

	@Override
	public List<ResponseCommentDto> findCommentsByRatingRange(Long orderId, Long userId, int min, int max) {

		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException("존재하지 않는 주문입니다.", HttpStatus.NOT_FOUND));
		if (!order.getUser().equals(userId)) {
			throw new CustomException("조회 권한이 없습니다.", HttpStatus.BAD_REQUEST);
		}
		if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
			throw new CustomException("배달완료가되지 않았습니다.", HttpStatus.BAD_REQUEST);
		}
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createAt"));
		Page<Comment> comments = commentRepository.findByRatingBetweenAndDeletedAtIsNull(min, max, pageRequest);
		return comments.stream().map(ResponseCommentDto::new).toList();
	}

	public List<ResponseCommentDto> findByOrderComments(Long orderId, Long userId) {

		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException("존재하지 않는 주문입니다.", HttpStatus.NOT_FOUND));
		if (!order.getUser().equals(userId)) {
			throw new CustomException("조회 권한이 없습니다.", HttpStatus.BAD_REQUEST);
		}
		if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
			throw new CustomException("배달완료가되지 않았습니다.", HttpStatus.BAD_REQUEST);
		}

		return commentRepository.findWithRelationsByOrderId(orderId)
			.stream()
			.map(ResponseCommentDto::new)
			.toList();
	}

	@Override
	public ResponseCommentDto updateComment(Long orderId, Long userId, Long commentId, RequestCommentDto dto) {

		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException("존재하지 않는 주문입니다.", HttpStatus.NOT_FOUND));
		if (!order.getUser().equals(userId)) {
			throw new CustomException("수정 권한이 없습니다.", HttpStatus.BAD_REQUEST);
		}
		if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
			throw new CustomException("배달완료가되지 않았습니다.", HttpStatus.BAD_REQUEST);
		}

		Comment findcomment = commentRepository
			.findByIdAndDeletedAtIsNull(commentId)
			.orElseThrow(() -> new CustomException("존재하지 않는 리뷰 입니다.", HttpStatus.NOT_FOUND));
		findcomment.update(dto);
		return new ResponseCommentDto(findcomment);
	}

	@Override
	public void deleteComment(Long orderId, Long userId, Long commentId) {

		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException("존재하지 않는 주문입니다.", HttpStatus.NOT_FOUND));
		if (!order.getUser().equals(userId)) {
			throw new CustomException("삭제 권한이 없습니다.", HttpStatus.BAD_REQUEST);
		}
		if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
			throw new CustomException("배달완료가되지 않았습니다.", HttpStatus.BAD_REQUEST);
		}

		//softDelete 진행.
		Comment findcomment = commentRepository
			.findByIdAndDeletedAtIsNull(commentId)
			.orElseThrow(() -> new CustomException("존재하지 않는 리뷰 입니다.", HttpStatus.NOT_FOUND));
		Status status = Status.fromValue(true);
		findcomment.updateDeleteStatus(status.getValue());
		findcomment.timeWhenDeleted();
	}
}
