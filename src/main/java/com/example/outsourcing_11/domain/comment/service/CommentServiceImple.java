package com.example.outsourcing_11.domain.comment.service;

import com.example.outsourcing_11.common.Status;
import com.example.outsourcing_11.common.exception.CustomException;
import com.example.outsourcing_11.common.exception.ErrorCode;
import com.example.outsourcing_11.domain.comment.dto.RequestCommentDto;
import com.example.outsourcing_11.domain.comment.dto.ResponseCommentDto;
import com.example.outsourcing_11.domain.comment.entity.Comment;
import com.example.outsourcing_11.domain.comment.repository.CommentRepository;
import com.example.outsourcing_11.domain.order.entity.Order;
import com.example.outsourcing_11.domain.order.entity.OrderStatus;
import com.example.outsourcing_11.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImple implements CommentService {

    private final OrderRepository orderRepository;
    private final CommentRepository commentRepository;

    public Order validateOrder(Long orderId, Long userId) {
        Order order = orderRepository.findByIdAndDeletedAtIsNull(orderId)
            .orElseThrow(() -> new CustomException(ErrorCode.ORDER_ALREADY_DELETED));
        if (!order.getUser().getId().equals(userId)) {
            throw new CustomException((ErrorCode.FORBIDDEN_PERMISSION));
        }
        if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
            throw new CustomException((ErrorCode.ORDER_NOT_COMPLETED));
        }
        return order;
    }

    @Override
    public ResponseCommentDto createComment(Long orderId, Long userId, RequestCommentDto dto) {

        validateOrder(orderId, userId);
        Comment savecomment = new Comment(dto);
        commentRepository.save(savecomment);
        return new ResponseCommentDto(savecomment);
    }

    @Override
    public List<ResponseCommentDto> findCommentsByRatingRange(Long orderId, Long userId, int min, int max) {

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> comments = commentRepository.findByRatingBetweenAndDeletedAtIsNull(min, max, pageRequest);
        return comments.stream().map(ResponseCommentDto::new).toList();
    }

    public List<ResponseCommentDto> findByOrderComments(Long orderId, Long userId) {

        validateOrder(orderId, userId);

        return commentRepository.findWithRelationsByOrderId(orderId)
            .stream()
            .map(ResponseCommentDto::new)
            .toList();
    }

    @Override
    public ResponseCommentDto updateComment(Long orderId, Long userId, Long commentId, RequestCommentDto dto) {

        validateOrder(orderId, userId);

        Comment findcomment = commentRepository
            .findByIdAndOrderIdAndDeletedAtIsNull(commentId, orderId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));
        findcomment.update(dto);
        commentRepository.save(findcomment);
        return new ResponseCommentDto(findcomment);
    }

    @Override
    public void deleteComment(Long orderId, Long userId, Long commentId) {

        validateOrder(orderId, userId);

        //softDelete 진행.
        Comment findcomment = commentRepository
            .findByIdAndOrderIdAndDeletedAtIsNull(commentId, orderId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));
        Status status = Status.fromValue(true);
        findcomment.updateDeleteStatus(status.getValue());
        findcomment.timeWhenDeleted();
        commentRepository.save(findcomment);
    }
}
