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
import com.example.outsourcing_11.common.exception.CustomException;
import com.example.outsourcing_11.config.security.CustomUserDetails;
import com.example.outsourcing_11.domain.ownercomment.dto.OwnerRequestCommentDto;
import com.example.outsourcing_11.domain.ownercomment.dto.OwnerResponseCommentDto;
import com.example.outsourcing_11.domain.ownercomment.service.OwnerServiceimple;

@RequestMapping("/stores/{storeId}/Comment")
@RestController
@RequiredArgsConstructor
public class OwnerCommentContoroller {

	private final OwnerServiceimple ownerServiceimple;

	@PostMapping("/{commentId}/Owner-reply")
	public ResponseEntity<OwnerResponseCommentDto> createOwnerComment(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long storeId,
		@RequestBody OwnerRequestCommentDto dto) {

		if (userDetails.getUser().equals(UserRole.OWNER)) {
			throw new CustomException("권한이 없습니다.", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(ownerServiceimple.createOwerComment(storeId, dto), HttpStatus.OK);
	}

	//가게의 모든 유저의 댓글
	@GetMapping()
	public ResponseEntity<List<OwnerResponseCommentDto>> getOwnerComment(
		@PathVariable Long storeId,
		@PathVariable Long commentId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		if (userDetails.getUser().equals(UserRole.OWNER)) {
			throw new CustomException("권한이 없습니다.", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(ownerServiceimple.getOwnerComment(storeId, commentId), HttpStatus.OK);
	}

	//유저 댓글에 대한 사장님 댓글 조회
	@GetMapping("/{commentId}/Owner-reply")
	public ResponseEntity<List<OwnerResponseCommentDto>> getOwnerComments(
		@PathVariable Long commentId,
		@PathVariable Long storeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails.getUser().equals(UserRole.OWNER)) {
			throw new CustomException("권한이 없습니다.", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(ownerServiceimple.getOwnerComments(storeId, storeId), HttpStatus.OK);
	}

	@PutMapping("/{commentId}/Owner-reply")
	public ResponseEntity<OwnerResponseCommentDto> updateOwnerComment(
		@PathVariable Long commentId,
		@PathVariable Long id,
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody OwnerRequestCommentDto dto) {
		if (userDetails.getUser().equals(UserRole.OWNER)) {
			throw new CustomException("권한이 없습니다.", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(ownerServiceimple.updateOwnerComment(commentId, dto), HttpStatus.OK);
	}

	@DeleteMapping("/{commentId}/Owner-reply")
	public ResponseEntity<Void> deleteOwnerComment(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long commentId) {
		if (userDetails.getUser().equals(UserRole.OWNER)) {
			throw new CustomException("권한이 없습니다.", HttpStatus.BAD_REQUEST);
		}
		ownerServiceimple.deleteOwerComment(commentId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
