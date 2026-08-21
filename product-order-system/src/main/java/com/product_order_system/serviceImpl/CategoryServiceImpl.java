package com.product_order_system.serviceImpl;

import com.product_order_system.dto.request.CategoryRequest;
import com.product_order_system.dto.response.CategoryResponse;
import com.product_order_system.entity.Category;
import com.product_order_system.exception.DuplicateResourceException;
import com.product_order_system.exception.ResourceNotFoundException;
import com.product_order_system.repository.CategoryRepository;
import com.product_order_system.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {

        if (categoryRepository.existsByNameIgnoreCase(
                request.getName())) {

            throw new DuplicateResourceException(
                    "Category already exists: "
                            + request.getName()
            );
        }

        Category category = new Category();

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(true);

        Category savedCategory =
                categoryRepository.save(category);

        return mapToResponse(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id
                        ));

        return mapToResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CategoryResponse updateCategory(
            Long id,
            CategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id
                        ));

        if (!category.getName()
                .equalsIgnoreCase(request.getName())
                && categoryRepository.existsByNameIgnoreCase(
                request.getName())) {

            throw new DuplicateResourceException(
                    "Category already exists: "
                            + request.getName()
            );
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updatedCategory =
                categoryRepository.save(category);

        return mapToResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id
                        ));

        categoryRepository.delete(category);
    }

    private CategoryResponse mapToResponse(
            Category category) {

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive()
        );
    }
}