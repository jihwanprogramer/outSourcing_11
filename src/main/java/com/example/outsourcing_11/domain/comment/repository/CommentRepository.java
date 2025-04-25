package com.example.outsourcing_11.domain.comment.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.outsourcing_11.domain.comment.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	//commentId에 해당하는 comment 조회
	Optional<Comment> findByIdAndDeletedAtIsNull(Long comentId);

	//별점 범위에 따른 comment 조회

	/**
	 2025.04.25
	 * 김형진
	 * 별점 범위 이면서 soft Delete가 적용된거 외에 것을 조회
	 * @param min ~0
	 * @param max ~5
	 * @return comments
	 */
	Page<Comment> findByRatingBetweenAndDeletedAtIsNull(int min, int max, Pageable pageable);

	@Query("SELECT c FROM Comment c JOIN Fetch  Order o WHERE o.id = :orderId AND c.isDeleted = false ")
	Optional<Comment> findWithRelationsByOrderId(@Param("orderId") Long orderId);

	default Comment findByOrThrowElse(Long commentId) {
		return findByIdAndDeletedAtIsNull(commentId).orElseThrow(() -> new RuntimeException("Temp Error"));
	}
}
