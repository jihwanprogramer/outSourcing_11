package com.example.outsourcing_11.domain.auth.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import com.example.outsourcing_11.domain.auth.dto.SignUpRequestDto;
import com.example.outsourcing_11.domain.auth.dto.SignUpResponseDto;
import com.example.outsourcing_11.domain.auth.exception.DuplicateUserException;
import com.example.outsourcing_11.domain.user.entity.User;
import com.example.outsourcing_11.domain.user.repository.UserRepository;
import com.example.outsourcing_11.global.config.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

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
}
