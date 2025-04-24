package com.example.outsourcing_11.domain.user.service;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import com.example.outsourcing_11.common.exception.user.UnauthorizedException;
import com.example.outsourcing_11.common.exception.user.UserNotFoundException;
import com.example.outsourcing_11.domain.user.dto.UserResponseDto;
import com.example.outsourcing_11.domain.user.entity.User;
import com.example.outsourcing_11.domain.user.repository.UserRepository;
import com.example.outsourcing_11.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	public UserResponseDto findUserById(Long userId) {
		User findUser = userRepository.findByIdOrElseThrow(userId);
		if (findUser.getDeletedAt() != null && !findUser.getStatusEnum().getValue()) {
			throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
		}

		return new UserResponseDto(findUser.getName(), findUser.getEmail(), findUser.getPhone(), findUser.getAddress(),
			findUser.getRole());
	}

	public UserResponseDto findLoginUserById(HttpServletRequest request) {
		// 1. 헤더에서 토큰 추출
		String token = request.getHeader("Authorization");
		if (!jwtUtil.validateToken(token)) {
			throw new UnauthorizedException("유효하지 않은 토큰입니다."); // 401 에러
		}
		// 토큰에서 userId 추출
		Long userId = jwtUtil.extractUserId(token);
		User findUser = userRepository.findByIdOrElseThrow(userId);

		if (findUser.getDeletedAt() != null && !findUser.getStatusEnum().getValue()) {
			throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
		}

		return new UserResponseDto(findUser.getName(), findUser.getEmail(), findUser.getPhone(), findUser.getAddress(),
			findUser.getRole());
	}
}
