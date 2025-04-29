package com.example.outsourcing_11.config.security;

import java.util.Collection;
import java.util.List;

import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.outsourcing_11.domain.user.entity.User;

@Getter
public class CustomUserDetails implements UserDetails {
	private final User user; // 실제 User 엔티티 보관

	public CustomUserDetails(User user) {
		this.user = user;
	}

	public Long getUserId() {
		return user.getId(); // 유저의 PK 반환
	}

	public String getRole() {
		return user.getRole().getRoleName(); // 고객 / 사장님 등
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 권한 정보 반환 (ROLE_ 접두어 붙여줌)
		return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()));
	}

	@Override
	public String getPassword() {
		return user.getPassword(); // 유저 비밀번호
	}

	@Override
	public String getUsername() {
		return user.getEmail(); // 유저 이메일을 아이디로 사용
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // 계정 만료 여부 (true면 만료되지 않음)
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // 계정 잠김 여부
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // 비밀번호 만료 여부
	}

	@Override
	public boolean isEnabled() {
		return true; // 계정 활성화 여부
	}
}
