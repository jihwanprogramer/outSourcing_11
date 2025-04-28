package com.example.outsourcing_11.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.outsourcing_11.common.exception.CustomException;
import com.example.outsourcing_11.common.exception.ErrorCode;
import com.example.outsourcing_11.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	// 이메일로 조회
	Optional<User> findByEmail(String email);//

	// Id로 조회
	Optional<User> findById(Long id);//

	// 이메일통해 존재하는지만 판단
	boolean existsByEmail(String email);

	// Id통해 존재하는지만 판단
	boolean existsById(Long id);

	// email로 조회
	default User findByEmailOrElseThrow(String email) {
		return findByEmail(email).orElseThrow(() ->
			new CustomException(ErrorCode.USER_NOT_FOUND)); // 조회 실패시(해당 email 없을 때) 404, "존재하지 않는 정보입니다."
	}

	// id로 조회
	default User findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() ->
			new CustomException(ErrorCode.USER_NOT_FOUND)); // 조회 실패시(해당 id 없을 때) 404, "존재하지 않는 정보입니다."
	}

	@EntityGraph(attributePaths = {"storeList"})
	@Query("SELECT u FROM User u WHERE u.id = :userId")
	Optional<User> findOwnerWithStores(@Param("userId") Long userId);
}
