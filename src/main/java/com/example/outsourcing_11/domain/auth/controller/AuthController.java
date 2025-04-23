package com.example.outsourcing_11.domain.auth.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.outsourcing_11.domain.auth.dto.LoginRequestDto;
import com.example.outsourcing_11.domain.auth.dto.SignUpRequestDto;
import com.example.outsourcing_11.domain.auth.dto.SignUpResponseDto;
import com.example.outsourcing_11.domain.auth.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<SignUpResponseDto> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
		SignUpResponseDto response = authService.signUp(requestDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto) {
		String accessToken = authService.login(requestDto);
		return new ResponseEntity<>(accessToken, HttpStatus.OK);
	}

}
