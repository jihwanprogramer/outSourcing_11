package com.example.outsourcing_11.domain.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.outsourcing_11.domain.comment.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	Optional<Comment> findByIdAndDeletedAtIsNull(Long id);

	// // 존재하는 유저 중 , comment가 삭제도 안됬으면서 별점의 범위로 조회.
	@EntityGraph(attributePaths = {"user"})
	List<Comment> findByRatingBetweenAndDeletedAtIsNull(int min, int max);

	//
	// // 주문상태가 완료이면서 , comment가 삭제도 안된 경우
	// //@EntityGraph(attributePaths = {"order"})
	//
	default Comment findByOrThrowElse(Long commentId) {
		return findByIdAndDeletedAtIsNull(commentId).orElseThrow(() -> new RuntimeException("Temp Error"));
	}
}
