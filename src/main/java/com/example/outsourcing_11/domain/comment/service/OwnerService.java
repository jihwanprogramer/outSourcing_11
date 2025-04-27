package com.example.outsourcing_11.domain.comment.service;

import java.util.List;

import com.example.outsourcing_11.domain.comment.dto.Owner.OwnerRequestCommentDto;
import com.example.outsourcing_11.domain.comment.dto.Owner.OwnerResponseCommentDto;

public interface OwnerService {

	OwnerResponseCommentDto creatOwerComment(OwnerRequestCommentDto dto);

	List<OwnerResponseCommentDto> getOwerComment(Long storeId, Long commentId);

	List<OwnerResponseCommentDto> getOwerComments(Long id);

	OwnerResponseCommentDto updateOwerComment(Long storeId, OwnerRequestCommentDto dto);

	void deleteOwerComment(Long id);
}
