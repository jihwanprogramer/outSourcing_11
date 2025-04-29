package com.example.outsourcing_11.domain.user.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;

import com.example.outsourcing_11.common.Base;
import com.example.outsourcing_11.common.Status;
import com.example.outsourcing_11.common.UserRole;
import com.example.outsourcing_11.domain.store.entity.Store;

@Getter
@Entity
@Table(name = "users")
public class User extends Base {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String phone;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, columnDefinition = "varchar(100) default 'CUSTOMER'")
	private UserRole role;

	@Column(nullable = false)
	private String address;

	@Column(name = "status", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private boolean status = Status.EXIST.getValue();

	@OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
	private List<Store> storeList; //사장님일 경우 가지고있는 가게도 같이 조회

	public User() {

	}

	public User(String name, String email, String password, String phone, String address, UserRole role) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.address = address;
		this.role = role;
	}

	public User(String email, String password, String name, UserRole role) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.role = role;
	}

	// 현재 boolean status를 enum 형태로 반환 (가독성/비즈니스 로직용)
	public Status getStatus() {
		return Status.fromValue(this.status);
	}

	// 회원을 탈퇴(비활성화) 상태로 전환하는 도메인 메서드
	public void softDelete() {
		this.deletedAt = LocalDateTime.now();
		this.status = Status.NON_EXIST.getValue();
	}

	public void updatePassword(String password) {
		this.password = password;
	}

	public void updateAddress(String address) {
		this.address = address;
	}

	public void updatePhone(String phone) {
		this.phone = phone;
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void updateEmail(String email) {
		this.email = email;
	}

}
