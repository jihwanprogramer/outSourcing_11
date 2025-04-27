package com.example.outsourcing_11.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.outsourcing_11.config.security.CustomUserDetails;
import com.example.outsourcing_11.domain.user.dto.DeleteAndUpdateUserResponseDto;
import com.example.outsourcing_11.domain.user.dto.PasswordRequestDto;
import com.example.outsourcing_11.domain.user.dto.UpdateUserAddressRequestDto;
import com.example.outsourcing_11.domain.user.dto.UpdateUserPasswordRequestDto;
import com.example.outsourcing_11.domain.user.dto.UpdateUserRequestDto;
import com.example.outsourcing_11.domain.user.dto.UserResponseDto;
import com.example.outsourcing_11.domain.user.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/{userId}")
	public ResponseEntity<UserResponseDto> findUserById(@PathVariable Long userId) {
		UserResponseDto responseDto = userService.findUserById(userId);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@GetMapping("/checkLogin")
	public ResponseEntity<UserResponseDto> findLoginUserById(@AuthenticationPrincipal CustomUserDetails userDetails) {
		UserResponseDto responseDto = userService.findLoginUserById(userDetails);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@PostMapping("/delete-auth")
	public ResponseEntity<String> issueDeleteAuthCookie(
		@RequestBody PasswordRequestDto passwordDto,
		@AuthenticationPrincipal CustomUserDetails userDetails,
		HttpServletResponse response) {
		userService.issueDeleteAuthCookie(passwordDto, userDetails, response);
		return new ResponseEntity<>("수정 및 삭제 인증 쿠키 발급 완료", HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateUser(
		@RequestBody UpdateUserRequestDto updateUserRequestDto,
		@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
		userService.updateUser(updateUserRequestDto, userDetails, request);
		return new ResponseEntity<>("회원 정보 수정 완료", HttpStatus.OK);
	}

	@PatchMapping("/updateAddress")
	public ResponseEntity<String> updateUserAddress(
		@RequestBody UpdateUserAddressRequestDto requestDto,
		@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
		userService.updateUserAddress(requestDto, userDetails, request);
		return new ResponseEntity<>("회원 정보 수정 완료", HttpStatus.OK);
	}

	@PatchMapping("/updatePassword")
	public ResponseEntity<String> updateUserPassword(
		@RequestBody UpdateUserPasswordRequestDto requestDto,
		@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
		userService.updateUserPassword(requestDto, userDetails, request);
		return new ResponseEntity<>("회원 정보 수정 완료", HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<DeleteAndUpdateUserResponseDto> softDeleteUser(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		HttpServletRequest request) {
		DeleteAndUpdateUserResponseDto responseDto = userService.softDeleteUser(userDetails, request);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
}
