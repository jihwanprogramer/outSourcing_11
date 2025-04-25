package com.example.outsourcing_11.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.outsourcing_11.config.security.JwtAccessDeniedHandler;
import com.example.outsourcing_11.config.security.JwtAuthenticationEntryPoint;
import com.example.outsourcing_11.config.security.JwtAuthenticationFilter;
import com.example.outsourcing_11.domain.user.repository.UserRepository;
import com.example.outsourcing_11.util.JwtUtil;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtUtil jwtUtil; // JWT 유틸 주입
	private final UserRepository userRepository; // 유저 조회용 레포지토리 주입
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		// JwtAuthenticationFilter Bean 수동 등록 (생성자 주입)
		return new JwtAuthenticationFilter(jwtUtil, userRepository);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			//  CSRF 토큰 비활성화 (JWT 기반 API에서는 보통 사용 안 함)
			.csrf(AbstractHttpConfigurer::disable)
			//  세션 사용하지 않도록 설정 (JWT 방식이기 때문)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			//  요청 권한 설정
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/auth/**").permitAll() // 로그인/회원가입 등은 인증 필요 없음
				.anyRequest().authenticated() // 나머지는 인증 필요
			)
			.exceptionHandling(ex -> ex
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.accessDeniedHandler(jwtAccessDeniedHandler)
			)
			//  JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 등록
			.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
