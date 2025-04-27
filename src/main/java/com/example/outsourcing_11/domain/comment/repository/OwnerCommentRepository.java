package com.example.outsourcing_11.domain.comment.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.outsourcing_11.domain.comment.entity.OwnerComment;

@Repository
public interface OwnerCommentRepository extends JpaRepository<OwnerComment, Long> {

	@EntityGraph(attributePaths = {"user"})
	Optional<OwnerComment> findById(Long userId);

	//고객들이 남긴 댓글을 사장님이 보는 기능.
	@Query("""
		SELECT c FROM Comment c
		JOIN c.order o
		JOIN o.store s
		WHERE s.id = :storeId AND c.id = :commentId AND c.isDeleted = false""")
	Optional<OwnerComment> findByStoreIdAndCommentId(@Param("storeId") Long storeId,
		@Param("commentId") Long commentId);

	//사장님의 대댓글 전체 조회.
	@Query("""
		SELECT oc FROM OwnerComment oc 
		JOIN oc.comment c
		JOIN c.order o
		JOIN o.store s
		WHERE s.id = :storeId AND oc.isDeleted = false""")
	Page<OwnerComment> findByStoreIdAndDeletedFalse(@Param("storeId") Long storeId, Pageable pageable);
}
