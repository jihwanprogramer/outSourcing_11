package com.example.outsourcing_11.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.outsourcing_11.config.security.JwtAccessDeniedHandler;
import com.example.outsourcing_11.config.security.JwtAuthenticationEntryPoint;
import com.example.outsourcing_11.config.security.JwtAuthenticationFilter;
import com.example.outsourcing_11.domain.user.repository.UserRepository;
import com.example.outsourcing_11.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private UserRepository userRepository;

	@Mock
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Mock
	private JwtAccessDeniedHandler jwtAccessDeniedHandler;

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
