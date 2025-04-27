package com.example.outsourcing_11.domain.comment.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
		return new OwnerResponseCommentDto(ownerComment);
	}

	@Override
	public List<OwnerResponseCommentDto> getOwerComment(Long storeId, Long commentId) {

		List<OwnerResponseCommentDto> ownerComments = ownerCommentRepository.findByStoreIdAndCommentId(storeId,
				commentId)
			.stream()
			.map(OwnerResponseCommentDto::new)
			.toList();

		return ownerComments;
	}

	@Override
	public List<OwnerResponseCommentDto> getOwerComments(Long storeId) {

		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createAt"));

		Page<OwnerComment> ownerComment = ownerCommentRepository.findByStoreIdAndDeletedFalse(storeId,
			pageRequest);

		return ownerComment.stream()
			.map(OwnerResponseCommentDto::new)
			.toList();
	}

	@Override
	public OwnerResponseCommentDto updateOwerComment(Long id, OwnerRequestCommentDto dto) {

		OwnerComment ownerComment = ownerCommentRepository.findById(id).orElseThrow();
		ownerComment.updateContent(dto.getContent());
		return new OwnerResponseCommentDto(ownerComment);
	}

	@Override
	public void deleteOwerComment(Long id) {

		OwnerComment findOwnerComment = ownerCommentRepository.findById(id).orElseThrow();
		Status status = Status.fromValue(true);
		findOwnerComment.updateDeleteStatus(status.getValue());
		findOwnerComment.timeWhenDeleted();
	}
}
