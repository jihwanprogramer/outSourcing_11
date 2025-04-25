package com.example.outsourcing_11.domain.comment.service;

import com.example.outsourcing_11.domain.comment.dto.Owner.OwnerRequestCommentDto;
import com.example.outsourcing_11.domain.comment.dto.Owner.OwnerResponseCommentDto;

public interface OwnerService {

	OwnerResponseCommentDto creatOwerComment(OwnerRequestCommentDto dto);

	OwnerResponseCommentDto updateOwerComment(Long id, OwnerRequestCommentDto dto);

	void deleteOwerComment(Long id);
}
