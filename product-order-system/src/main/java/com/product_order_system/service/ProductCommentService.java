package com.product_order_system.service;

import com.product_order_system.dto.request.CommentRequest;
import com.product_order_system.dto.response.CommentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductCommentService {

    List<CommentResponse> getCommentsByProduct(Long productId);

    CommentResponse addComment(Long productId, CommentRequest request);

    CommentResponse updateComment(Long commentId, CommentRequest request);

    void deleteComment(Long commentId);
}