package com.example.outsourcing_11.domain.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

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
import com.example.outsourcing_11.domain.auth.dto.LoginRequestDto;
import com.example.outsourcing_11.domain.auth.dto.SignUpRequestDto;
import com.example.outsourcing_11.domain.auth.service.AuthService;
import com.example.outsourcing_11.domain.user.entity.User;
import com.example.outsourcing_11.domain.user.repository.UserRepository;
import com.example.outsourcing_11.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private JwtUtil jwtUtil;
	@InjectMocks
	private AuthService authService;

	@Test
	void 정상회원가입() {
		// given
		String email = "newuser@test.com";
		String password = "1234";
		String encodedPassword = "encoded1234";

		SignUpRequestDto requestDto = SignUpRequestDto.builder()
			.email(email)
			.password(password)
			.userName("신규유저")
			.role("고객")
			.build();  // 필요한 값만!

		given(userRepository.existsByEmail(email)).willReturn(false); // 이메일 중복 아님
		given(passwordEncoder.encode(password)).willReturn(encodedPassword); // 비번 인코딩 결과

		// when
		authService.signUp(requestDto);

		// then
		// 저장 메서드가 1번 호출됐는지 검증
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void 로그인_성공() {
		// given
		String email = "test@test.com";
		String rawPassword = "1234";
		String encodedPassword = "encoded-password";  // 저장된 암호화된 비밀번호
		String expectedToken = "mocked-access-token";

		User user = new User(email, encodedPassword, "테스트", UserRole.CUSTOMER);
		ReflectionTestUtils.setField(user, "id", 1L);

		given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
		given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);  // 비번 일치
		given(jwtUtil.generateAccessToken(user.getId(), user.getName(), user.getEmail(), user.getRole().getRoleName()))
			.willReturn(expectedToken);

		LoginRequestDto requestDto = new LoginRequestDto(email, rawPassword);

		// when
		String result = authService.login(requestDto);

		// then
		assertEquals(expectedToken, result);
	}

	@Test
	void 로그인실패_비밀번호불일치() {
		// given
		String email = "test@test.com";
		String rawPassword = "wrongPassword";
		String encodedPassword = "$2a$10$encoded"; // 예시 해시

		User user = new User(email, encodedPassword, "테스트", UserRole.CUSTOMER);
		ReflectionTestUtils.setField(user, "deletedAt", null);
		given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
		given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(false);

		LoginRequestDto requestDto = new LoginRequestDto(email, rawPassword);

		// when & then
		CustomException exception = assertThrows(CustomException.class, () -> {
			authService.login(requestDto);
		});

		// 추가: ErrorCode 검증
		assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
	}

	@Test
	void 회원가입실패_중복된이메일() {
		// given
		String email = "test2@test.com";
		String rawPassword = "wrongPassword";

		User user = new User("이름", email, rawPassword, "전화번호", "주소", UserRole.CUSTOMER);
		ReflectionTestUtils.setField(user, "deletedAt", null);
		given(userRepository.existsByEmail(email)).willReturn(true);

		SignUpRequestDto requestDto = new SignUpRequestDto("이름", email, rawPassword, "전화번호", "주소", "권한");

		// when & then
		CustomException exception = assertThrows(CustomException.class, () -> {
			authService.signUp(requestDto);
		});

		// 추가: ErrorCode 검증
		assertEquals(ErrorCode.DUPLICATE_USER, exception.getErrorCode());
	}
}
