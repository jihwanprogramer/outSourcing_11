package com.example.outsourcing_11.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.outsourcing_11.common.exception.user.UserNotFoundException;
import com.example.outsourcing_11.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	// 이메일로 조회
	Optional<User> findByEmail(String email);

	// id로 조회
	default User findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() ->
			new UserNotFoundException("존재하지 않는 정보입니다.")); // 조회 실패시(해당 id 없을 때) 404, "존재하지 않는 정보입니다."
	}

}
