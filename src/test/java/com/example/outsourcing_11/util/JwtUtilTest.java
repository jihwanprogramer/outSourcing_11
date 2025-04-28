package com.example.outsourcing_11.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

	private JwtUtil jwtUtil;
	private String token;

	@BeforeEach
	void setUp() {
		jwtUtil = new JwtUtil();
		token = jwtUtil.generateAccessToken(1L, "김석진", "test@test.com", "고객");
	}

	@Test
	void 토큰_생성_및_유효성_검증() {
		// then
		assertNotNull(token);
		assertTrue(jwtUtil.validateToken(token));
	}

	@Test
	void 토큰에서_userId_추출() {
		// when
		Long userId = jwtUtil.extractUserId(token);

		// then
		assertEquals(1L, userId);
	}

	@Test
	void 토큰에서_userName_추출() {
		// when
		String userName = jwtUtil.extractUserNameFromToken(token);

		// then
		assertEquals("김석진", userName);
	}

	@Test
	void 토큰에서_email_추출() {
		// when
		String email = jwtUtil.extractEmailFromToken(token);

		// then
		assertEquals("test@test.com", email);
	}

	@Test
	void 토큰에서_role_추출() {
		// when
		String role = jwtUtil.extractRoleFromToken(token);

		// then
		assertEquals("고객", role);
	}

	@Test
	void 유효하지_않은_토큰_검증() {
		// given
		String invalidToken = "invalid.token.value";

		// then
		assertFalse(jwtUtil.validateToken(invalidToken));
	}
}
