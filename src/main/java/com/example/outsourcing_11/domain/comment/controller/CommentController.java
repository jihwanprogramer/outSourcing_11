package com.example.outsourcing_11.domain.comment.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.outsourcing_11.domain.comment.dto.RequestCommentDto;
import com.example.outsourcing_11.domain.comment.dto.ResponseCommentDto;
import com.example.outsourcing_11.domain.comment.service.CommentServiceImple;

@RequestMapping
@RestController
@RequiredArgsConstructor
public class CommentController {

	private static CommentServiceImple commentServiceImple;

	@PostMapping("stores/{storeId}/orders/{orderId}/comment")
	public ResponseEntity<ResponseCommentDto> creatComment(
		@PathVariable Long storeId,
		@PathVariable Long orderId,
		@RequestBody RequestCommentDto dto) {

		return new ResponseEntity<ResponseCommentDto>(commentServiceImple.createComment(storeId, orderId, dto),
			HttpStatus.CREATED);
	}

	@GetMapping("stores/{storeId}/orders/{orderId}/comment")
	public ResponseEntity<List<ResponseCommentDto>> getComment(
		@PathVariable Long storeId,
		@PathVariable Long orderId,
		@RequestParam int min,
		@RequestParam int max) {

		return new ResponseEntity<>(commentServiceImple.findCommentsByRatingRange(min, max), HttpStatus.OK);
	}

	@PutMapping("stores/{storeId}/orders/{orderId}/comment/{commentId}")
	public ResponseEntity<ResponseCommentDto> putComment(
		@PathVariable Long storeId,
		@PathVariable Long orderId,
		@PathVariable Long commentId,
		@RequestBody RequestCommentDto dto) {

		return new ResponseEntity<>(commentServiceImple.updateComment(commentId, dto), HttpStatus.OK);
	}

	@DeleteMapping("stores/{storeId}/orders/{orderId}/comment/{commentId}")
	public ResponseEntity<Void> deleteComment(
		@PathVariable Long storeId,
		@PathVariable Long orderId,
		@PathVariable Long commentId) {
		commentServiceImple.deleteComment(commentId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
