package com.example.outsourcing_11.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import com.example.outsourcing_11.common.Status;
import com.example.outsourcing_11.common.UserRole;
import com.example.outsourcing_11.common.exception.user.UserNotFoundException;
import com.example.outsourcing_11.config.security.CustomUserDetails;
import com.example.outsourcing_11.domain.user.dto.UserResponseDto;
import com.example.outsourcing_11.domain.user.entity.User;
import com.example.outsourcing_11.domain.user.repository.UserRepository;
import com.example.outsourcing_11.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;
	@Mock
	private HttpServletRequest request;
	@Mock
	private JwtUtil jwtUtil;

	@Test
	void 유저조회_성공() {
		// given
		Long userId = 1L;

		User user = new User("test@test.com", "password", "테스트이름", UserRole.CUSTOMER);

		// id는 DB에서 생성되는 거니까 테스트용으로 reflection으로 넣을 수 있음
		ReflectionTestUtils.setField(user, "id", userId);
		ReflectionTestUtils.setField(user, "deletedAt", null);

		given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);

		// when
		UserResponseDto result = userService.findUserById(userId);
		// then
		assertEquals(user.getEmail(), result.getEmail());
	}

	@Test
	void 로그인유저조회_성공() {
		// given
		Long userId = 1L;
		User user = new User("홍길동", "test@test.com", "1234", "010-0000-0000", "서울", UserRole.CUSTOMER);
		ReflectionTestUtils.setField(user, "id", userId);
		ReflectionTestUtils.setField(user, "deletedAt", null);

		CustomUserDetails userDetails = new CustomUserDetails(user);
		given(userRepository.findByIdOrElseThrow(userId)).willReturn(user);

		// when
		UserResponseDto result = userService.findLoginUserById(userDetails);

		// then
		assertEquals("test@test.com", result.getEmail());
		assertEquals("홍길동", result.getUserName());
	}

	@Test
	void 로그인유저조회_실패_삭제된유저() {
		// given
		Long userId = 1L;
		User deletedUser = new User("홍길동", "test@test.com", "1234", "010", "서울", UserRole.CUSTOMER);
		ReflectionTestUtils.setField(deletedUser, "id", userId);
		ReflectionTestUtils.setField(deletedUser, "deletedAt", LocalDateTime.now());
		ReflectionTestUtils.setField(deletedUser, "status", Status.NON_EXIST.getValue());

		CustomUserDetails userDetails = new CustomUserDetails(deletedUser);
		given(userRepository.findByIdOrElseThrow(userId)).willReturn(deletedUser);

		// when & then
		assertThrows(UserNotFoundException.class, () -> {
			userService.findLoginUserById(userDetails);
		});
	}
}
