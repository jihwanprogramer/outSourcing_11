package com.example.outsourcing_11.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import com.example.outsourcing_11.common.UserRole;
import com.example.outsourcing_11.common.exception.CustomException;
import com.example.outsourcing_11.common.exception.ErrorCode;
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

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private JwtUtil jwtUtil;
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;

	@InjectMocks
	private UserService userService;

	private User user;

	@BeforeEach
	void setUp() {
		user = new User("test@test.com", "password", "테스트이름", UserRole.CUSTOMER);
		ReflectionTestUtils.setField(user, "id", 1L);
		ReflectionTestUtils.setField(user, "deletedAt", null);
	}

	@Test
	void 유저조회_성공() {
		// given
		given(userRepository.findByIdOrElseThrow(1L)).willReturn(user);

		// when
		UserResponseDto result = userService.findUserById(1L);

		// then
		assertEquals("test@test.com", result.getEmail());
	}

	@Test
	void 유저조회_실패_삭제된유저() {
		// given
		ReflectionTestUtils.setField(user, "deletedAt", LocalDateTime.now());
		given(userRepository.findByIdOrElseThrow(1L)).willReturn(user);

		// when & then
		CustomException exception = assertThrows(CustomException.class, () -> {
			userService.findUserById(1L);
		});
		assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	void 로그인유저조회_성공() {
		// given
		CustomUserDetails userDetails = new CustomUserDetails(user);
		given(userRepository.findByIdOrElseThrow(1L)).willReturn(user);

		// when
		UserResponseDto result = userService.findLoginUserById(userDetails);

		// then
		assertEquals("테스트이름", result.getUserName());
	}

	@Test
	void 삭제인증쿠키발급_성공() {
		// given
		PasswordRequestDto passwordRequestDto = new PasswordRequestDto("password");
		CustomUserDetails userDetails = new CustomUserDetails(user);

		given(userRepository.findByIdOrElseThrow(1L)).willReturn(user);
		given(passwordEncoder.matches("password", "password")).willReturn(true);

		// when
		assertDoesNotThrow(() -> {
			userService.issueDeleteAuthCookie(passwordRequestDto, userDetails, response);
		});
	}

	@Test
	void 삭제인증쿠키발급_실패_비밀번호불일치() {
		// given
		PasswordRequestDto passwordRequestDto = new PasswordRequestDto("wrongpassword");
		CustomUserDetails userDetails = new CustomUserDetails(user);

		given(userRepository.findByIdOrElseThrow(1L)).willReturn(user);
		given(passwordEncoder.matches("wrongpassword", "password")).willReturn(false);

		// when & then
		CustomException exception = assertThrows(CustomException.class, () -> {
			userService.issueDeleteAuthCookie(passwordRequestDto, userDetails, response);
		});
		assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
	}

	@Test
	void 유저정보수정_실패_쿠키없음() {
		// given
		UpdateUserRequestDto requestDto = new UpdateUserRequestDto("newName", "new@test.com", "01012345678");
		CustomUserDetails userDetails = new CustomUserDetails(user);
		given(request.getCookies()).willReturn(null);

		// when & then
		CustomException exception = assertThrows(CustomException.class, () -> {
			userService.updateUser(requestDto, userDetails, request);
		});
		assertEquals(ErrorCode.UNAUTHORIZED_COOKIE, exception.getErrorCode());
	}

	@Test
	void 유저주소수정_실패_쿠키없음() {
		// given
		UpdateUserAddressRequestDto requestDto = new UpdateUserAddressRequestDto("서울특별시");
		CustomUserDetails userDetails = new CustomUserDetails(user);
		given(request.getCookies()).willReturn(null);

		// when & then
		CustomException exception = assertThrows(CustomException.class, () -> {
			userService.updateUserAddress(requestDto, userDetails, request);
		});
		assertEquals(ErrorCode.UNAUTHORIZED_COOKIE, exception.getErrorCode());
	}

	@Test
	void 유저비밀번호수정_실패_쿠키없음() {
		// given
		UpdateUserPasswordRequestDto requestDto = new UpdateUserPasswordRequestDto("oldPassword", "newPassword");
		CustomUserDetails userDetails = new CustomUserDetails(user);
		given(request.getCookies()).willReturn(null);

		// when & then
		CustomException exception = assertThrows(CustomException.class, () -> {
			userService.updateUserPassword(requestDto, userDetails, request);
		});
		assertEquals(ErrorCode.UNAUTHORIZED_COOKIE, exception.getErrorCode());
	}

	@Test
	void 회원탈퇴_실패_쿠키없음() {
		// given
		CustomUserDetails userDetails = new CustomUserDetails(user);
		given(request.getCookies()).willReturn(null);

		// when & then
		CustomException exception = assertThrows(CustomException.class, () -> {
			userService.softDeleteUser(userDetails, request);
		});
		assertEquals(ErrorCode.UNAUTHORIZED_COOKIE, exception.getErrorCode());
	}

	@Test
	void 유저정보수정_성공() {
		// given
		UpdateUserRequestDto requestDto = new UpdateUserRequestDto("새이름", "newemail@test.com", "01099998888");
		CustomUserDetails userDetails = new CustomUserDetails(user);

		Cookie cookie = new Cookie("delete_auth", "true");
		given(request.getCookies()).willReturn(new Cookie[] {cookie});
		given(userRepository.findByIdOrElseThrow(1L)).willReturn(user);

		// when
		assertDoesNotThrow(() -> {
			userService.updateUser(requestDto, userDetails, request);
		});
	}

	@Test
	void 유저주소수정_성공() {
		// given
		UpdateUserAddressRequestDto requestDto = new UpdateUserAddressRequestDto("서울특별시 강남구");
		CustomUserDetails userDetails = new CustomUserDetails(user);

		Cookie cookie = new Cookie("delete_auth", "true");
		given(request.getCookies()).willReturn(new Cookie[] {cookie});
		given(userRepository.findByIdOrElseThrow(1L)).willReturn(user);

		// when
		assertDoesNotThrow(() -> {
			userService.updateUserAddress(requestDto, userDetails, request);
		});
	}

	@Test
	void 유저비밀번호수정_성공() {
		// given
		UpdateUserPasswordRequestDto requestDto = new UpdateUserPasswordRequestDto("oldPassword", "newPassword");
		CustomUserDetails userDetails = new CustomUserDetails(user);

		Cookie cookie = new Cookie("delete_auth", "true");
		given(request.getCookies()).willReturn(new Cookie[] {cookie});
		given(userRepository.findByIdOrElseThrow(1L)).willReturn(user);
		given(passwordEncoder.matches("oldPassword", user.getPassword())).willReturn(true); // old 비번 일치
		given(passwordEncoder.matches("newPassword", user.getPassword())).willReturn(false); // 새 비번 다름
		given(passwordEncoder.encode("newPassword")).willReturn("encodedNewPassword"); // 새 비번 인코딩

		// when
		assertDoesNotThrow(() -> {
			userService.updateUserPassword(requestDto, userDetails, request);
		});
	}

	@Test
	void 회원탈퇴_성공() {
		// given
		CustomUserDetails userDetails = new CustomUserDetails(user);

		Cookie cookie = new Cookie("delete_auth", "true");
		given(request.getCookies()).willReturn(new Cookie[] {cookie});
		given(userRepository.findByIdOrElseThrow(1L)).willReturn(user);

		// when
		DeleteAndUpdateUserResponseDto result = userService.softDeleteUser(userDetails, request);

		// then
		assertEquals("회원 탈퇴 완료", result.getMessage());
		assertNotNull(result.getDeletedAt()); // 탈퇴한 시간 확인
	}

}
