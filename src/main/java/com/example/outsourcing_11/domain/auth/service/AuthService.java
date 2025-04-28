package com.example.outsourcing_11.domain.auth.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import com.example.outsourcing_11.common.UserRole;
import com.example.outsourcing_11.common.exception.user.UserCustomException;
import com.example.outsourcing_11.common.exception.user.UserErrorCode;
import com.example.outsourcing_11.config.PasswordEncoder;
import com.example.outsourcing_11.domain.auth.dto.LoginRequestDto;
import com.example.outsourcing_11.domain.auth.dto.SignUpRequestDto;
import com.example.outsourcing_11.domain.auth.dto.SignUpResponseDto;
import com.example.outsourcing_11.domain.user.entity.User;
import com.example.outsourcing_11.domain.user.repository.UserRepository;
import com.example.outsourcing_11.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
		if (userRepository.existsByEmail(requestDto.getEmail())) {
			throw new UserCustomException(UserErrorCode.DUPLICATE_USER);
		}

		String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
		User user = new User(
			requestDto.getUserName(),
			requestDto.getEmail(),
			encodedPassword,
			requestDto.getPhone(),
			requestDto.getAddress(),
			UserRole.from(requestDto.getRole())
		);
		userRepository.save(user);

		return new SignUpResponseDto(
			requestDto.getUserName(),
			requestDto.getEmail(),
			requestDto.getPhone(),
			requestDto.getAddress(),
			requestDto.getRole(),
			user.getCreatedAt());
	}

	// 로그인 (Access Token 발급)
	public String login(LoginRequestDto requestDto) {
		User user = userRepository.findByEmail(requestDto.getEmail())
			.orElseThrow(() -> new UserCustomException(UserErrorCode.USER_NOT_FOUND));

		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new UserCustomException(UserErrorCode.INVALID_PASSWORD);
		}
		if (user.getDeletedAt() != null || user.getStatus().getValue()) {
			throw new UserCustomException(UserErrorCode.UNAUTHORIZED_ACCESS);
		}

		return jwtUtil.generateAccessToken(user.getId(), user.getName(), user.getEmail(), user.getRole().getRoleName());
	}
}
