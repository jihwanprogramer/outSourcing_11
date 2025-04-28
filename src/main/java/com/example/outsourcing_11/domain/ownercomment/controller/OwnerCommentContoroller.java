package com.example.outsourcing_11.domain.ownercomment.controller;

import com.example.outsourcing_11.common.UserRole;
import com.example.outsourcing_11.common.exception.CustomException;
import com.example.outsourcing_11.common.exception.ErrorCode;
import com.example.outsourcing_11.config.security.CustomUserDetails;
import com.example.outsourcing_11.domain.ownercomment.dto.OwnerRequestCommentDto;
import com.example.outsourcing_11.domain.ownercomment.dto.OwnerResponseCommentDto;
import com.example.outsourcing_11.domain.ownercomment.service.OwnerServiceimple;
import com.example.outsourcing_11.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        return new ResponseEntity<>(ownerServiceimple.createOwerComment(commentId, dto), HttpStatus.OK);
    }

    //사장님 대댓글 전부 조회
    @GetMapping("/{commentId}/Owner-reply")
    public ResponseEntity<List<OwnerResponseCommentDto>> getOwnerComments(
        @PathVariable Long storeId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails.getUser().getRole().equals(UserRole.OWNER)) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        return new ResponseEntity<>(ownerServiceimple.getOwnerComments(storeId), HttpStatus.OK);
    }

    @PutMapping("/{commentId}/Owner-reply")
    public ResponseEntity<OwnerResponseCommentDto> updateOwnerComment(
        @PathVariable Long commentId,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody OwnerRequestCommentDto dto) {

        if (userDetails.getUser().getRole().equals(UserRole.OWNER)) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }
        return new ResponseEntity<>(ownerServiceimple.updateOwnerComment(commentId, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}/Owner-reply")
    public ResponseEntity<Void> deleteOwnerComment(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long commentId) {

        if (userDetails.getUser().getRole().equals(UserRole.OWNER)) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        ownerServiceimple.deleteOwerComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
