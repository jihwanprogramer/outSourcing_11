package com.example.outsourcing_11.domain.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.outsourcing_11.domain.store.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
	@Query("SELECT n FROM Notice n JOIN FETCH n.store WHERE n.id = :id")
	Optional<Notice> findByIdWithStore(@Param("id") Long id);
}
