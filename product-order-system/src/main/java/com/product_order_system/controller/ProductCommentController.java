package com.product_order_system.controller;

import com.product_order_system.dto.request.CommentRequest;
import com.product_order_system.dto.response.CommentResponse;
import com.product_order_system.service.ProductCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductCommentController {

    private final ProductCommentService commentService;

    @GetMapping("/products/{productId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(
                commentService.getCommentsByProduct(productId)
        );
    }

    @PostMapping("/products/{productId}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long productId,
            @Valid @RequestBody CommentRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.addComment(productId, request));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request
    ) {
        return ResponseEntity.ok(
                commentService.updateComment(commentId, request)
        );
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
