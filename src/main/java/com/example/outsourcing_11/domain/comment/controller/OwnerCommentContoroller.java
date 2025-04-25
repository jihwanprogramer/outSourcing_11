package com.example.outsourcing_11.domain.comment.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.outsourcing_11.domain.comment.dto.Owner.OwnerRequestCommentDto;
import com.example.outsourcing_11.domain.comment.dto.Owner.OwnerResponseCommentDto;
import com.example.outsourcing_11.domain.comment.service.OwnerServiceimple;

@RequestMapping("stores/{storeId}/")
@RestController
@RequiredArgsConstructor
public class OwnerCommentContoroller {

	private final OwnerServiceimple ownerServiceimple;

	@PostMapping("orders/{orderId}/comments{commentId}/repley")
	private ResponseEntity<OwnerResponseCommentDto> createOwnerComment(@RequestBody OwnerRequestCommentDto dto) {

		return new ResponseEntity<>(ownerServiceimple.creatOwerComment(dto), HttpStatus.OK);
	}

	@PutMapping("orders/{orderId}/comments{commentId}/repley/{id}")
	private ResponseEntity<OwnerResponseCommentDto> updateOwnerComment(
		@PathVariable Long id,
		@RequestBody OwnerRequestCommentDto dto) {

		return new ResponseEntity<>(ownerServiceimple.updateOwerComment(id, dto), HttpStatus.OK);
	}

	@DeleteMapping("stores/{storeId}/orders/{orderId}/comments{commentId}/repley/{id}")
	private ResponseEntity<Void> deleteOwnerComment(@PathVariable Long id) {

		ownerServiceimple.deleteOwerComment(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
