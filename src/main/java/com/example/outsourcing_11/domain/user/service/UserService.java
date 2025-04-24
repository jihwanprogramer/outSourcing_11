package com.example.outsourcing_11.domain.user.service;

import java.util.Arrays;
import java.util.Optional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import com.example.outsourcing_11.common.exception.user.InvalidLoginException;
import com.example.outsourcing_11.common.exception.user.UnauthorizedException;
import com.example.outsourcing_11.common.exception.user.UserNotFoundException;
import com.example.outsourcing_11.config.PasswordEncoder;
import com.example.outsourcing_11.domain.user.dto.DeleteUserResponseDto;
import com.example.outsourcing_11.domain.user.dto.PasswordRequestDto;
import com.example.outsourcing_11.domain.user.dto.UserResponseDto;
import com.example.outsourcing_11.domain.user.entity.User;
import com.example.outsourcing_11.domain.user.repository.UserRepository;
import com.example.outsourcing_11.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
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
		//  헤더에서 토큰 추출
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

	public void issueDeleteAuthCookie(PasswordRequestDto passwordDto,
		HttpServletRequest request,
		HttpServletResponse response) {
		//  헤더에서 토큰 추출
		String token = request.getHeader("Authorization");
		if (!jwtUtil.validateToken(token)) {
			throw new UnauthorizedException("유효하지 않은 토큰입니다."); // 401 에러
		}
		// 토큰에서 userId 추출
		Long userId = jwtUtil.extractUserId(token);
		User user = userRepository.findByIdOrElseThrow(userId);

		// 비밀번호 확인
		if (!passwordEncoder.matches(passwordDto.getPassword(), user.getPassword())) {
			throw new InvalidLoginException("비밀번호가 일치하지 않습니다.");
		}

		// 쿠키 발급 (3분짜리)
		Cookie cookie = new Cookie("delete_auth", "true");
		cookie.setMaxAge(3 * 60); // 180초
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	@Transactional
	public DeleteUserResponseDto softDeleteUser(HttpServletRequest request) {
		// 쿠키 확인
		boolean valid = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
			.anyMatch(cookie -> "delete_auth".equals(cookie.getName()) && "true".equals(cookie.getValue()));

		if (!valid) {
			throw new UnauthorizedException("삭제 인증 쿠키가 없습니다.");
		}

		// 유저 삭제
		String token = request.getHeader("Authorization");
		Long userId = jwtUtil.extractUserId(token);
		User user = userRepository.findByIdOrElseThrow(userId);
		user.softDelete();

		return new DeleteUserResponseDto("회원 탈퇴 완료", user.getDeletedAt());
	}
}
