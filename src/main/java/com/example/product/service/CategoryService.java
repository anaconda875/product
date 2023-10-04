package com.example.product.service;

import com.example.product.dto.request.CategoryRequest;
import com.example.product.dto.response.CategoryResponse;

public interface CategoryService {

  CategoryResponse save(CategoryRequest request);
  CategoryResponse findById(Long id);

}
