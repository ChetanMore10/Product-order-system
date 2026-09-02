package com.product_order_system.serviceImpl;

import com.product_order_system.dto.request.CommentRequest;
import com.product_order_system.dto.response.CommentResponse;
import com.product_order_system.entity.Product;
import com.product_order_system.entity.ProductComment;
import com.product_order_system.entity.User;
import com.product_order_system.exception.BadRequestException;
import com.product_order_system.exception.ResourceNotFoundException;
import com.product_order_system.repository.ProductCommentRepository;
import com.product_order_system.repository.ProductRepository;
import com.product_order_system.repository.UserRepository;
import com.product_order_system.service.ProductCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCommentServiceImpl implements ProductCommentService {

    private final ProductCommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByProduct(Long productId) {

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(
                    "Product not found with id: " + productId
            );
        }

        return commentRepository
                .findByProductIdAndParentCommentIsNullOrderByCreatedAtDesc(productId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CommentResponse addComment(
            Long productId,
            CommentRequest request
    ) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + productId
                        )
                );

        User user = getAuthenticatedUser();

        ProductComment comment = new ProductComment();

        comment.setProduct(product);
        comment.setUser(user);
        comment.setComment(request.getComment());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        // Create reply if parentCommentId is provided
        if (request.getParentCommentId() != null) {

            ProductComment parentComment =
                    commentRepository.findById(
                            request.getParentCommentId()
                    ).orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Parent comment not found with id: "
                                            + request.getParentCommentId()
                            )
                    );

            // Parent comment must belong to the same product
            if (!parentComment.getProduct().getId().equals(productId)) {
                throw new BadRequestException(
                        "Parent comment does not belong to this product"
                );
            }

            comment.setParentComment(parentComment);
        }

        ProductComment savedComment =
                commentRepository.save(comment);

        return mapToResponse(savedComment);
    }

    @Override
    public CommentResponse updateComment(
            Long commentId,
            CommentRequest request
    ) {

        ProductComment comment =
                commentRepository.findById(commentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Comment not found with id: "
                                                + commentId
                                )
                        );

        User currentUser = getAuthenticatedUser();

        // User can update only their own comment
        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new BadRequestException(
                    "You can update only your own comment"
            );
        }

        comment.setComment(request.getComment());
        comment.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long commentId) {

        ProductComment comment =
                commentRepository.findById(commentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Comment not found with id: "
                                                + commentId
                                )
                        );

        User currentUser = getAuthenticatedUser();

        // User can delete only their own comment
        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new BadRequestException(
                    "You can delete only your own comment"
            );
        }

        commentRepository.delete(comment);
    }

    private User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new BadRequestException(
                    "User is not authenticated"
            );
        }

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Authenticated user not found"
                        )
                );
    }

    private CommentResponse mapToResponse(
            ProductComment comment
    ) {

        List<CommentResponse> replies =
                commentRepository
                        .findByParentCommentIdOrderByCreatedAtAsc(
                                comment.getId()
                        )
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        Long parentCommentId =
                comment.getParentComment() != null
                        ? comment.getParentComment().getId()
                        : null;

        return new CommentResponse(
                comment.getId(),
                comment.getProduct().getId(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getComment(),
                parentCommentId,
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                replies
        );
    }
}