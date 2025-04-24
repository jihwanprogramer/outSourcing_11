package com.example.outsourcing_11.domain.comment.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import com.example.outsourcing_11.common.Status;
import com.example.outsourcing_11.domain.comment.dto.RequestCommentDto;
import com.example.outsourcing_11.domain.comment.dto.ResponseCommentDto;
import com.example.outsourcing_11.domain.comment.entity.Comment;
import com.example.outsourcing_11.domain.comment.repository.CommentRepository;
import com.example.outsourcing_11.domain.order.entity.Order;
import com.example.outsourcing_11.domain.order.repository.OrderRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImple implements CommentService {

	private static CommentRepository commentRepository;
	private static OrderRepository orderRepository;

	@Override
	public ResponseCommentDto createComment(Long storeId, Long orderId, RequestCommentDto dto) {
		//주문상태를 확인하고 Complete 아닐경우 예외 발생하기.

		Order order = orderRepository.getReferenceById(orderId);
		// if(!order..equals(storeId)){
		// 	return  new RuntimeException("주문한 가계가 아닙니다.", HttpStatus.BAD_REQUEST);
		// }
		// if(!order.getUser().getId().equals(userId)){
		// 	return  new RuntimeException("권한이 없습니다.",HttpStatus.BAD_REQUEST);
		// }
		// if (!order.getStatus() == "Complete") {
		// 	return new RuntimeException("배달이 완료되지 않았습니다.", HttpStatus.BAD_REQUEST);
		// }

		Comment savecomment = new Comment(dto);
		commentRepository.save(savecomment);
		return new ResponseCommentDto(savecomment);
	}

	@Override
	public List<ResponseCommentDto> findCommentsByRatingRange(int min, int max) {

		// Order order = findByOrder(orderId);
		// if(!order.getStore().store.getId().equals(storeId)){
		// 	return  new RuntimeException("주문한 가계가 아닙니다.", HttpStatus.BAD_REQUEST);
		// }
		// if(!order.getUser().getId().equals(userId)){
		// 	return  new RuntimeException("조회 권한이 없습니다.", HttpStatus.BAD_REQUEST);
		// }
		// if (!order.) {
		// 	return new RuntimeException("배달이 완료되지 않았습니다.", HttpStatus.BAD_REQUEST);
		// }
		List<Comment> comments = commentRepository.findByRatingBetweenAndDeletedAtIsNull(min, max);
		return comments.stream().map(ResponseCommentDto::new).toList();
	}

	@Override
	public ResponseCommentDto updateComment(Long commentId, RequestCommentDto dto) {

		// Order order = findByOrder(orderId);
		// if(!order.getStore().store.getId().equals(storeId)){
		// 	return  new RuntimeException("주문한 가계가 아닙니다.", HttpStatus.BAD_REQUEST);
		// }
		// if(!order.getUser().getId().equals(userId)){
		// 	return  new RuntimeException("수정 권한이 없습니다.",HttpStatus.BAD_REQUEST);
		// }
		// if (!order.getStatus() == "Complete") {
		// 	return new RuntimeException("배달이 완료되지 않았습니다.", HttpStatus.BAD_REQUEST);
		// }
		// Comment findcomment = commentRepository.findByOrThrowElse(commentId);
		// findcomment = new Comment(dto);
		return null;
	}

	@Override
	public void deleteComment(Long commentId) {

		// Order order = findByOrder(orderId);
		// if(!order.getStore().store.getId().equals(storeId)){
		// 	return  new RuntimeException("주문한 가계가 아닙니다.", HttpStatus.BAD_REQUEST);
		// }
		// if(!order.getUser().getId().equals(userId)){
		// 	return  new RuntimeException("수정 권한이 없습니다.",HttpStatus.BAD_REQUEST);
		// }
		// if (!order.getStatus().equals("Complete")) {
		// 	return new RuntimeException("배달이 완료되지 않았습니다.", HttpStatus.BAD_REQUEST);
		// }
		//softDelete 진행.
		Comment findcomment = commentRepository.findByOrThrowElse(commentId);
		Status status = Status.fromValue(true);
		findcomment.updateDeleteStatus(status.getValue());
		findcomment.timeWhenDeleted();
	}
}
