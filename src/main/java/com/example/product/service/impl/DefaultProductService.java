package com.example.product.service.impl;

import com.example.product.domain.model.Category;
import com.example.product.domain.model.Product;
import com.example.product.dto.request.ProductRequest;
import com.example.product.dto.response.ProductResponse;
import com.example.product.exception.ResourceNotFoundException;
import com.example.product.repository.CategoryRepository;
import com.example.product.repository.ProductRepository;
import com.example.product.service.CategoryService;
import com.example.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

    private final CategoryService categoryService;

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

}
