package com.example.outsourcing_11.domain.comment.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.outsourcing_11.config.security.CustomUserDetails;
import com.example.outsourcing_11.domain.comment.dto.RequestCommentDto;
import com.example.outsourcing_11.domain.comment.dto.ResponseCommentDto;
import com.example.outsourcing_11.domain.comment.service.CommentServiceImple;
import com.example.outsourcing_11.domain.user.service.UserService;

@RequestMapping("/stores/{storeId}")
@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentServiceImple commentServiceImple;
	private final UserService userService;

	@PostMapping("/orders/{orderId}/comment")
	public ResponseEntity<ResponseCommentDto> creatComment(
		@PathVariable Long orderId,
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody RequestCommentDto dto) {
		return new ResponseEntity<>(commentServiceImple.createComment(orderId, userDetails.getUserId(), dto),
			HttpStatus.CREATED);
	}

	@GetMapping("/orders/{orderId}/comment")
	public ResponseEntity<List<ResponseCommentDto>> getComment(
		@PathVariable Long orderId,
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam int min,
		@RequestParam int max) {
		return new ResponseEntity<>(
			commentServiceImple.findCommentsByRatingRange(orderId, userDetails.getUserId(), min, max),
			HttpStatus.OK);
	}

	@PutMapping("/orders/{orderId}/comment/{commentId}")
	public ResponseEntity<ResponseCommentDto> putComment(
		@PathVariable Long orderId,
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long commentId,
		@RequestBody RequestCommentDto dto) {

		return new ResponseEntity<>(commentServiceImple.updateComment(orderId, userDetails.getUserId(), commentId, dto),
			HttpStatus.OK);
	}

	@DeleteMapping("/orders/{orderId}/comment/{commentId}")
	public ResponseEntity<Void> deleteComment(
		@PathVariable Long orderId,
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long commentId) {
		commentServiceImple.deleteComment(orderId, userDetails.getUserId(), commentId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
