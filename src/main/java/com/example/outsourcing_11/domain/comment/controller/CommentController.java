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

@RequestMapping("stores/")
@RestController
@RequiredArgsConstructor
public class CommentController {

	private static CommentServiceImple commentServiceImple;

	@PostMapping
	public ResponseEntity<ResponseCommentDto> creatComment(@RequestBody RequestCommentDto dto) {

		return new ResponseEntity<ResponseCommentDto>(commentServiceImple.createComment(dto), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<ResponseCommentDto>> getComment(
		@RequestParam int min,
		@RequestParam int max) {

		return new ResponseEntity<>(commentServiceImple.findCommentsByRatingRange(min, max), HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<ResponseCommentDto> putComment(
		@PathVariable Long commentId,
		@RequestBody RequestCommentDto dto) {

		return new ResponseEntity<>(commentServiceImple.updateComment(commentId, dto), HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
		commentServiceImple.deleteComment(commentId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
