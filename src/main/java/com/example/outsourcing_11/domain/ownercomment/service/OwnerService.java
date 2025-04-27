package com.example.outsourcing_11.domain.ownercomment.service;

import java.util.List;

import com.example.outsourcing_11.domain.ownercomment.dto.OwnerRequestCommentDto;
import com.example.outsourcing_11.domain.ownercomment.dto.OwnerResponseCommentDto;

public interface OwnerService {

	OwnerResponseCommentDto createOwerComment(Long commentId, OwnerRequestCommentDto dto);

	List<OwnerResponseCommentDto> getOwnerComment(Long storeId, Long commentId);

	List<OwnerResponseCommentDto> getOwnerComments(Long storeId, Long commentId);

	OwnerResponseCommentDto updateOwnerComment(Long commentId, OwnerRequestCommentDto dto);

	void deleteOwerComment(Long commentId);
}
