package com.product_order_system.repository;

import com.product_order_system.entity.ProductComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCommentRepository extends JpaRepository<ProductComment, Long> {

    List<ProductComment> findByProductIdAndParentCommentIsNullOrderByCreatedAtDesc(Long productId);

    List<ProductComment> findByParentCommentIdOrderByCreatedAtAsc(Long parentCommentId);
}
