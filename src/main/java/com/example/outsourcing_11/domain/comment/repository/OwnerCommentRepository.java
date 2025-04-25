package com.example.outsourcing_11.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.outsourcing_11.domain.comment.entity.OwnerComment;

@Repository
public interface OwnerCommentRepository extends JpaRepository<OwnerComment, Long> {
}
