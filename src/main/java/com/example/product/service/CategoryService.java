package com.example.product.service;

import com.example.product.dto.request.CategoryRequest;
import com.example.product.dto.request.CustomPageable;
import com.example.product.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;

public interface CategoryService {

  CategoryResponse save(CategoryRequest request);

  CategoryResponse findById(Long id);

  Page<CategoryResponse> findAll(CustomPageable pageable);
}
