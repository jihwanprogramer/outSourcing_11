package com.example.outsourcing_11.domain.user.service;

import java.util.Arrays;
import java.util.Optional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import com.example.outsourcing_11.common.UserRole;
import com.example.outsourcing_11.common.exception.user.InvalidLoginException;
import com.example.outsourcing_11.common.exception.user.InvalidUserInputException;
import com.example.outsourcing_11.common.exception.user.UnauthorizedException;
import com.example.outsourcing_11.common.exception.user.UserNotFoundException;
import com.example.outsourcing_11.config.PasswordEncoder;
import com.example.outsourcing_11.config.security.CustomUserDetails;
import com.example.outsourcing_11.domain.user.dto.DeleteAndUpdateUserResponseDto;
import com.example.outsourcing_11.domain.user.dto.PasswordRequestDto;
import com.example.outsourcing_11.domain.user.dto.UpdateUserAddressRequestDto;
import com.example.outsourcing_11.domain.user.dto.UpdateUserPasswordRequestDto;
import com.example.outsourcing_11.domain.user.dto.UpdateUserRequestDto;
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

	// 일반 조회기능
	public UserResponseDto findUserById(Long userId) {
		User findUser = userRepository.findByIdOrElseThrow(userId);
		if (findUser.getDeletedAt() != null || findUser.getStatus().getValue()) {
			throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
		}

		return new UserResponseDto(findUser.getName(), findUser.getEmail(), findUser.getPhone(), findUser.getAddress(),
			findUser.getRole().getRoleName());
	}

	//로그인된 사용자 조회
	public UserResponseDto findLoginUserById(CustomUserDetails userDetails) {
		// 토큰에서 userId 추출
		Long userId = userDetails.getUserId();
		User findUser = userRepository.findByIdOrElseThrow(userId);

		if (findUser.getDeletedAt() != null || findUser.getStatus().getValue()) {
			throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
		}

		if (findUser.getRole() == UserRole.OWNER) {
			findUser = userRepository.findOwnerWithStores(userId)
				.orElseThrow(() -> new UserNotFoundException("사장님 정보를 가져오는 데 실패했습니다."));
		}

		return new UserResponseDto(findUser);
	}

	//수정전용 비밀번호 확인인증 쿠키
	public void issueDeleteAuthCookie(PasswordRequestDto passwordDto,
		CustomUserDetails userDetails,
		HttpServletResponse response) {
		// 토큰에서 userId 추출
		Long userId = userDetails.getUserId();
		User user = userRepository.findByIdOrElseThrow(userId);

		// 비밀번호 확인
		if (!passwordEncoder.matches(passwordDto.getPassword(), user.getPassword())) {
			throw new InvalidLoginException("비밀번호가 일치하지 않습니다.");
		}

		// 쿠키 발급 (3분짜리) 삭제인증 전용쿠키
		Cookie cookie = new Cookie("delete_auth", "true");
		cookie.setMaxAge(3 * 60); // 180초
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	// 이름 이메일(로그인아이디) 전화번호 수정
	@Transactional
	public void updateUser(UpdateUserRequestDto requestdto, CustomUserDetails userDetails, HttpServletRequest request) {
		// 쿠키 인증
		boolean valid = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
			.anyMatch(cookie -> "delete_auth".equals(cookie.getName()) && "true".equals(cookie.getValue()));

		if (!valid) {
			throw new UnauthorizedException("수정 및 삭제 인증 쿠키가 없습니다.");
		}

		// 유저 조회
		Long userId = userDetails.getUserId();
		User user = userRepository.findByIdOrElseThrow(userId);

		// 변경 사항만 업데이트
		if (!requestdto.getUserName().equals(user.getName())) {
			user.updateName(requestdto.getUserName());
		}

		if (!requestdto.getEmail().equals(user.getEmail())) {
			user.updateEmail(requestdto.getEmail());
		}

		if (!requestdto.getPhone().equals(user.getPhone())) {
			user.updatePhone(requestdto.getPhone());
		}
	}

	//주소수정
	@Transactional
	public void updateUserAddress(UpdateUserAddressRequestDto requestDto, CustomUserDetails userDetails,
		HttpServletRequest request) {
		// 쿠키 인증
		boolean valid = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
			.anyMatch(cookie -> "delete_auth".equals(cookie.getName()) && "true".equals(cookie.getValue()));

		if (!valid) {
			throw new UnauthorizedException("수정 및 삭제 인증 쿠키가 없습니다.");
		}

		// 유저 조회
		Long userId = userDetails.getUserId();
		User user = userRepository.findByIdOrElseThrow(userId);

		user.updateAddress(requestDto.getNewAddress());
	}

	//비번수정
	@Transactional
	public void updateUserPassword(UpdateUserPasswordRequestDto requestDto, CustomUserDetails userDetails,
		HttpServletRequest request) {
		// 쿠키 인증
		boolean valid = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
			.anyMatch(cookie -> "delete_auth".equals(cookie.getName()) && "true".equals(cookie.getValue()));

		if (!valid) {
			throw new UnauthorizedException("수정 및 삭제 인증 쿠키가 없습니다.");
		}

		// 유저 조회
		Long userId = userDetails.getUserId();
		User user = userRepository.findByIdOrElseThrow(userId);

		if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) { // 비번체크
			throw new InvalidLoginException("비밀번호가 일치하지 않습니다"); // 401 반환
		}

		if (passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())) {
			throw new InvalidUserInputException("현재 비밀번호와 동일합니다"); // 400 반환
		}
		user.updatePassword(passwordEncoder.encode(requestDto.getNewPassword()));
	}

	//쿠키를 통한 소프트 삭제
	@Transactional
	public DeleteAndUpdateUserResponseDto softDeleteUser(CustomUserDetails userDetails, HttpServletRequest request) {
		// 쿠키 확인
		boolean valid = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
			.anyMatch(cookie -> "delete_auth".equals(cookie.getName()) && "true".equals(cookie.getValue()));

		if (!valid) {
			throw new UnauthorizedException("수정 및 삭제 인증 쿠키가 없습니다.");
		}

		// 유저 삭제
		Long userId = userDetails.getUserId();
		User user = userRepository.findByIdOrElseThrow(userId);
		user.softDelete();

		return new DeleteAndUpdateUserResponseDto("회원 탈퇴 완료", user.getDeletedAt());
	}
}
