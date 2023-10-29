package com.example.product.service.impl;

import com.example.product.domain.model.Category;
import com.example.product.domain.model.Product;
import com.example.product.dto.request.ProductRequest;
import com.example.product.dto.response.ProductResponse;
import com.example.product.exception.ResourceNotFoundException;
import com.example.product.repository.CategoryRepository;
import com.example.product.repository.ProductRepository;
import com.example.product.service.ProductService;
import com.example.product.service.UniqueValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultProductService implements ProductService, UniqueValidationService<String> {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    @Override
    public ProductResponse save(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Product entity = this.from(request);
        entity.setCategory(category);

        productRepository.save(entity);
        return new ProductResponse(entity);
    }

    private Product from(ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .amount(request.getAmount())
                .status(request.getStatus())
                .build();
    }

    @Override
    public List<String> findInvalidFields(String name) {
        int count = productRepository.countByName(name);
        return count > 0 ? List.of(name) : Collections.emptyList();
    }
}
