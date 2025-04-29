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

	Optional<Comment> findByIdAndIsDeletedFalse(Long commentId);

	/**
	 * commentId에 해당하는 comment 조회
	 *
	 * @param commentId
	 * @return
	 */
	Optional<Comment> findByIdAndOrderIdAndDeletedAtIsNull(Long commentId, Long orderId);

	/**
	 * 2025.04.25
	 * 김형진
	 * 별점 범위 이면서 soft Delete가 적용된거 외에 것을 조회
	 *
	 * @param min ~0
	 * @param max ~5
	 * @return comments
	 */
	Page<Comment> findByRatingBetweenAndDeletedAtIsNull(int min, int max, Pageable pageable);

	/**
	 * 주문에 존재하는 comment를 전부 조회 (대댓글 조회 하지 않음)
	 *
	 * @param orderId Long orederId
	 * @return Optional로 받아서 내보냄
	 */
	@Query("SELECT c FROM Comment c JOIN  c.order o WHERE o.id = :orderId AND c.isDeleted = false ORDER BY c.createdAt DESC")
	Optional<Comment> findWithRelationsByOrderId(@Param("orderId") Long orderId);

	/**
	 * 주문의 메뉴별 리뷰의 수를 반환
	 *
	 * @param menuId id 받기
	 * @return 개수를 반환
	 */
	@Query("SELECT COUNT(c) FROM Comment c JOIN c.order o JOIN o.items oi WHERE oi.menu.id = :menuId AND c.isDeleted = false")
	long countByMenuId(@Param("menuId") Long menuId);
}


