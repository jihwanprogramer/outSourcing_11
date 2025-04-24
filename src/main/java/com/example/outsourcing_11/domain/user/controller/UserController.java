package com.example.outsourcing_11.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.outsourcing_11.domain.user.dto.DeleteUserResponseDto;
import com.example.outsourcing_11.domain.user.dto.PasswordRequestDto;
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
	public ResponseEntity<UserResponseDto> findLoginUserById(HttpServletRequest request) {
		UserResponseDto responseDto = userService.findLoginUserById(request);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@PostMapping("/delete-auth")
	public ResponseEntity<String> issueDeleteAuthCookie(
		@RequestBody PasswordRequestDto passwordDto,
		HttpServletRequest request,
		HttpServletResponse response) {
		userService.issueDeleteAuthCookie(passwordDto, request, response);
		return new ResponseEntity<>("삭제 인증 쿠키 발급 완료", HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<DeleteUserResponseDto> softDeleteUser(HttpServletRequest request) {
		DeleteUserResponseDto responseDto = userService.softDeleteUser(request);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
}
