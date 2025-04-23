package com.example.outsourcing_11.common;

public enum Status {
	EXIST(true), NON_EXIST(false);

	private final boolean value;

	Status(boolean value) {
		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

	// boolean 값으로부터 enum 값 반환
	public static Status fromValue(boolean value) {
		return value ? EXIST : NON_EXIST;
	}
}
