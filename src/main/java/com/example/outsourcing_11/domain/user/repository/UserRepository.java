package com.example.outsourcing_11.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.outsourcing_11.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	// 이메일로 조회
	Optional<User> findByEmail(String email);

	Optional<User> findById(Long id);

}
