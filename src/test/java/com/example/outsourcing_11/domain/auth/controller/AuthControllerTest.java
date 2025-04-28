package com.example.outsourcing_11.domain.auth.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import com.example.outsourcing_11.domain.auth.dto.LoginRequestDto;
import com.example.outsourcing_11.domain.auth.dto.SignUpRequestDto;
import com.example.outsourcing_11.domain.auth.dto.SignUpResponseDto;
import com.example.outsourcing_11.domain.auth.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

	@InjectMocks
	private AuthController authController;

	@Mock
	private AuthService authService;

	@Test
	void 회원가입_성공() {
		// given
		SignUpRequestDto requestDto = new SignUpRequestDto("김석진", "test@test.com", "password", "01012345678", "서울",
			"고객");
		SignUpResponseDto expectedResponse = new SignUpResponseDto("회원가입 성공", "test@test.com");

		given(authService.signUp(requestDto)).willReturn(expectedResponse);

		// when
		ResponseEntity<SignUpResponseDto> response = authController.signUp(requestDto);

		// then
		assertEquals(200, response.getStatusCodeValue());
		assertEquals(expectedResponse, response.getBody());
	}

	@Test
	void 로그인_성공() {
		// given
		LoginRequestDto requestDto = new LoginRequestDto("test@test.com", "password");
		String accessToken = "mockAccessToken";

		given(authService.login(requestDto)).willReturn(accessToken);

		// when
		ResponseEntity<String> response = authController.login(requestDto);

		// then
		assertEquals(200, response.getStatusCodeValue());
		assertEquals("Authorization: Bearer " + accessToken, response.getBody());
	}
}
