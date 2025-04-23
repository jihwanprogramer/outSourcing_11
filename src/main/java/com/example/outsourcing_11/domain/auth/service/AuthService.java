package com.example.outsourcing_11.domain.auth.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import com.example.outsourcing_11.common.exception.user.DuplicateUserException;
import com.example.outsourcing_11.common.exception.user.InvalidLoginException;
import com.example.outsourcing_11.common.exception.user.UserNotFoundException;
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
		if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
			throw new DuplicateUserException("이미 존재하는 사용자 이메일입니다.");
		}

		String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
		User user = new User(
			requestDto.getUserName(),
			requestDto.getEmail(),
			encodedPassword,
			requestDto.getPhone(),
			requestDto.getRoel(),
			requestDto.getAddress()
		);
		userRepository.save(user);

		return new SignUpResponseDto(
			requestDto.getUserName(),
			requestDto.getEmail(),
			requestDto.getPhone(),
			requestDto.getAddress(),
			requestDto.getRoel(),
			user.getCreatedAt());
	}

	// 로그인 (Access Token 발급)
	public String login(LoginRequestDto requestDto) {
		User user = userRepository.findByEmail(requestDto.getEmail())
			.orElseThrow(() -> new UserNotFoundException("일치하는 유저를 찾을 수 없음"));

		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new InvalidLoginException("비밀번호가 일치하지 않음");
		}

		return jwtUtil.generateAccessToken(user.getId(), user.getName());
	}
}
