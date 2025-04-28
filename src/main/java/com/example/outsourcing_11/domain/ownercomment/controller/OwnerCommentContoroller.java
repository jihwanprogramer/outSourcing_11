package com.example.outsourcing_11.domain.ownercomment.controller;

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
import org.springframework.web.bind.annotation.RestController;
import com.example.outsourcing_11.common.UserRole;
import com.example.outsourcing_11.common.exception.ErrorCode;
import com.example.outsourcing_11.common.exception.comment.AccessDeniedException;
import com.example.outsourcing_11.config.security.CustomUserDetails;
import com.example.outsourcing_11.domain.ownercomment.dto.OwnerRequestCommentDto;
import com.example.outsourcing_11.domain.ownercomment.dto.OwnerResponseCommentDto;
import com.example.outsourcing_11.domain.ownercomment.service.OwnerServiceimple;
import com.example.outsourcing_11.domain.user.service.UserService;

@RequestMapping("/stores/{storeId}/Comment")
@RestController
@RequiredArgsConstructor
public class OwnerCommentContoroller {

	private final OwnerServiceimple ownerServiceimple;
	private final UserService userService;

	@PostMapping("/{commentId}/Owner-reply")
	public ResponseEntity<OwnerResponseCommentDto> createOwnerComment(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long commentId,
		@RequestBody OwnerRequestCommentDto dto) {

		if (userDetails.getUser().getRole().equals(UserRole.OWNER)) {
			throw new AccessDeniedException(ErrorCode.FORBIDDEN_PERMISSION.getMessage());
		}

		return new ResponseEntity<>(ownerServiceimple.createOwerComment(commentId, dto), HttpStatus.OK);
	}

	//사장님 대댓글 전부 조회
	@GetMapping("/{commentId}/Owner-reply")
	public ResponseEntity<List<OwnerResponseCommentDto>> getOwnerComments(
		@PathVariable Long storeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		if (userDetails.getUser().getRole().equals(UserRole.OWNER)) {
			throw new AccessDeniedException(ErrorCode.FORBIDDEN_PERMISSION.getMessage());
		}

		return new ResponseEntity<>(ownerServiceimple.getOwnerComments(storeId), HttpStatus.OK);
	}

	@PutMapping("/{commentId}/Owner-reply")
	public ResponseEntity<OwnerResponseCommentDto> updateOwnerComment(
		@PathVariable Long commentId,
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody OwnerRequestCommentDto dto) {

		if (userDetails.getUser().getRole().equals(UserRole.OWNER)) {
			throw new AccessDeniedException(ErrorCode.FORBIDDEN_PERMISSION.getMessage());
		}
		return new ResponseEntity<>(ownerServiceimple.updateOwnerComment(commentId, dto), HttpStatus.OK);
	}

	@DeleteMapping("/{commentId}/Owner-reply")
	public ResponseEntity<Void> deleteOwnerComment(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long commentId) {

		if (userDetails.getUser().getRole().equals(UserRole.OWNER)) {
			throw new AccessDeniedException(ErrorCode.FORBIDDEN_PERMISSION.getMessage());
		}
		
		ownerServiceimple.deleteOwerComment(commentId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
