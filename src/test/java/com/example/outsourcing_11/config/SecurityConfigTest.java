package com.example.outsourcing_11.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.outsourcing_11.config.security.JwtAuthenticationFilter;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

	@InjectMocks
	private SecurityConfig securityConfig;

	@Test
	void jwtAuthenticationFilter_생성성공() {
		// when
		JwtAuthenticationFilter filter = securityConfig.jwtAuthenticationFilter();

		// then
		assertNotNull(filter);
	}
}
