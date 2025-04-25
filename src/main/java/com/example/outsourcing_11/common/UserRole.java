package com.example.outsourcing_11.common;

import lombok.Getter;

@Getter
public enum UserRole {
	CUSTOMER("고객"),
	OWNER("사장님");

	private final String roleName;

	UserRole(String roleName) {
		this.roleName = roleName;
	}

	// DB에 저장된 문자열에서 enum 변환 (역매핑)
	public static UserRole from(String roleName) {
		for (UserRole role : values()) {
			if (role.roleName.equals(roleName)) {
				return role;
			}
		}
		throw new IllegalArgumentException("존재하지 않는 권한입니다: " + roleName);
	}
}
