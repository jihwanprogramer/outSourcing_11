package com.example.outsourcing_11.config.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.outsourcing_11.domain.user.entity.User;
import com.example.outsourcing_11.domain.user.repository.UserRepository;
import com.example.outsourcing_11.util.JwtUtil;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;

	public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
		this.jwtUtil = jwtUtil;
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		// 요청 헤더에서 Authorization 토큰 추출
		String token = request.getHeader("Authorization");

		//  토큰이 없거나 유효하지 않으면 401 에러 반환
		if (token == null || !jwtUtil.validateToken(token)) {
			filterChain.doFilter(request, response); // 그냥 다음 필터로 넘김
			return;
		}

		Long userId = jwtUtil.extractUserId(token); // 토큰에서 유저 ID 추출
		User user = userRepository.findById(userId)
			.filter(u -> u.getDeletedAt() == null && u.getStatus().getValue()) // 유효한 유저인지 확인
			.orElse(null);

		if (user != null) {
			CustomUserDetails userDetails = new CustomUserDetails(user);
			// 인증 객체 생성 후 SecurityContext에 등록
			UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		// 다음 필터로 요청 넘기기
		filterChain.doFilter(request, response);
	}
}
