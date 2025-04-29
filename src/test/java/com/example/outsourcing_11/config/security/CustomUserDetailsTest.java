package com.example.outsourcing_11.config.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.example.outsourcing_11.common.UserRole;
import com.example.outsourcing_11.domain.user.entity.User;

class CustomUserDetailsTest {

	private User user;
	private CustomUserDetails userDetails;

	@BeforeEach
	void setUp() {
		// given (테스트용 유저 객체 생성)
		user = new User("김석진", "test@test.com", "password123", "010-1234-5678", "서울", UserRole.CUSTOMER);
		userDetails = new CustomUserDetails(user);
	}

	@Test
	void getUserId_성공() {
		// when
		Long userId = userDetails.getUserId();

		// then
		assertEquals(user.getId(), userId); // 아직 id는 null일 수 있음 (reflection으로 세팅 가능)
	}

	@Test
	void getRole_성공() {
		// when
		String role = userDetails.getRole();

		// then
		assertEquals("고객", role); // UserRole.CUSTOMER → "고객"
	}

	@Test
	void getAuthorities_성공() {
		// when
		Collection authorities = userDetails.getAuthorities();

		// then
		assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_고객")));
	}

	@Test
	void getPassword_성공() {
		// when
		String password = userDetails.getPassword();

		// then
		assertEquals("password123", password);
	}

	@Test
	void getUsername_성공() {
		// when
		String username = userDetails.getUsername();

		// then
		assertEquals("test@test.com", username);
	}

	@Test
	void 계정_상태_항상_true() {
		assertTrue(userDetails.isAccountNonExpired());
		assertTrue(userDetails.isAccountNonLocked());
		assertTrue(userDetails.isCredentialsNonExpired());
		assertTrue(userDetails.isEnabled());
	}
}
