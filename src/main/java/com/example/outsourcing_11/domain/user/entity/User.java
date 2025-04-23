package com.example.outsourcing_11.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;

import com.example.outsourcing_11.common.Status;
import com.example.outsourcing_11.domain.Base;

@Getter
@Entity
@Table(name = "user")
public class User extends Base {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String phone;

	@Column(name = "role", nullable = false, columnDefinition = "varchar(100) DEFAULT '고객'")
	private String role = "고객";

	@Column(nullable = false)
	private String address;

	@Column(name = "status", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
	private boolean status = Status.EXIST.getValue();

	public User(String name, String email, String password, String phone, String role, String address) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.role = role;
		this.address = address;
	}

	public User() {

	}

	// 현재 boolean status를 enum 형태로 반환 (가독성/비즈니스 로직용)
	public Status getStatusEnum() {
		return Status.fromValue(this.status);
	}

	// enum을 통해 상태를 설정하고 내부적으로는 boolean에 저장
	public void setStatusEnum(Status memberStatus) {
		this.status = memberStatus.getValue();
	}

	// 회원을 탈퇴(비활성화) 상태로 전환하는 도메인 메서드
	public void deactivate() {
		this.status = Status.NON_EXIST.getValue();
	}

	// 현재 회원이 활성 상태인지 확인
	public boolean isActive() {
		return this.status == Status.EXIST.getValue();
	}
}
