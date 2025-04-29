package com.example.outsourcing_11.common.exception.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {

	// 회원 관련 에러코드
	USER_NOT_FOUND("NF-001", "사용자를 찾을 수 없습니다.", 404),

	DUPLICATE_USER("BAD-001", "이미 존재하는 사용자 이메일입니다.", 400),
	SAME_PASSWORD("BAD-002", "현재 비밀번호와 동일합니다.", 400),
	INVALID_PASSWORD("BAD-003", "비밀번호가 일치하지 않습니다.", 401),
	UNAUTHORIZED_ACCESS("AUTH-001", "탈퇴한 회원 정보입니다.", 403),
	UNAUTHORIZED_COOKIE("AUTH-002", "수정 및 삭제 인증 쿠키가 없습니다.", 401),
	AUTHORIZATION_HEADER_MISSING("AUTH-003", "Authorization 헤더가 비어 있습니다.", 401);

	private final String code;
	private final String message;
	private final int status;
}
