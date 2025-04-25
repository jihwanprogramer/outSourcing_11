package com.example.outsourcing_11.domain.comment.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import com.example.outsourcing_11.common.Status;
import com.example.outsourcing_11.domain.comment.dto.Owner.OwnerRequestCommentDto;
import com.example.outsourcing_11.domain.comment.dto.Owner.OwnerResponseCommentDto;
import com.example.outsourcing_11.domain.comment.entity.OwnerComment;
import com.example.outsourcing_11.domain.comment.repository.OwnerCommentRepository;

@Service
@RequiredArgsConstructor
public class OwnerServiceimple implements OwnerService {

	private final OwnerCommentRepository ownerCommentRepository;

	@Override
	public OwnerResponseCommentDto creatOwerComment(OwnerRequestCommentDto dto) {

		OwnerComment ownerComment = new OwnerComment(dto);
		ownerCommentRepository.save(ownerComment);
		return new OwnerResponseCommentDto(ownerComment.getContent());
	}

	@Override
	public OwnerResponseCommentDto updateOwerComment(Long id, OwnerRequestCommentDto dto) {

		OwnerComment ownerComment = ownerCommentRepository.findById(id).orElseThrow();
		ownerComment.updateContent(dto.getContent());
		return new OwnerResponseCommentDto(ownerComment.getContent());
	}

	@Override
	public void deleteOwerComment(Long id) {

		OwnerComment findOwnerComment = ownerCommentRepository.findById(id).orElseThrow();
		Status status = Status.fromValue(true);
		findOwnerComment.updateDeleteStatus(status.getValue());
		findOwnerComment.timeWhenDeleted();
	}
}
