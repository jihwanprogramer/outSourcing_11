package com.example.outsourcing_11.domain.ownercomment.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.outsourcing_11.config.security.CustomUserDetails;
import com.example.outsourcing_11.domain.ownercomment.dto.OwnerRequestCommentDto;
import com.example.outsourcing_11.domain.ownercomment.dto.OwnerResponseCommentDto;
import com.example.outsourcing_11.domain.ownercomment.service.OwnerServiceimple;
import com.example.outsourcing_11.domain.user.service.UserService;

@RequestMapping("/stores/{storeId}/comments")
@RestController
@RequiredArgsConstructor
public class OwnerCommentController {

	private final OwnerServiceimple ownerServiceimple;
	private final UserService userService;

	@PostMapping("/{commentId}/ownerReplys")
	@PreAuthorize("hasRole('사장님')")
	public ResponseEntity<OwnerResponseCommentDto> createOwnerComment(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long commentId,
		@RequestBody OwnerRequestCommentDto dto) {

		return new ResponseEntity<>(ownerServiceimple.createOwerComment(commentId, dto), HttpStatus.OK);
	}

	//사장님 대댓글 전부 조회
	@GetMapping("/{commentId}/ownerReplys")
	public ResponseEntity<List<OwnerResponseCommentDto>> getOwnerComments(
		@PathVariable Long storeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		return new ResponseEntity<>(ownerServiceimple.getOwnerComments(storeId), HttpStatus.OK);
	}

	@PutMapping("/{commentId}/ownerReplys")
	@PreAuthorize("hasRole('사장님')")
	public ResponseEntity<OwnerResponseCommentDto> updateOwnerComment(
		@PathVariable Long commentId,
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody OwnerRequestCommentDto dto) {

		return new ResponseEntity<>(ownerServiceimple.updateOwnerComment(commentId, dto), HttpStatus.OK);
	}

	@DeleteMapping("/{commentId}/ownerReplys")
	public ResponseEntity<Void> deleteOwnerComment(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long commentId) {

		ownerServiceimple.deleteOwerComment(commentId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
