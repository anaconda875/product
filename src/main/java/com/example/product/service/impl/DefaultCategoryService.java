package com.example.product.service.impl;

import com.example.product.domain.model.Category;
import com.example.product.dto.request.CategoryRequest;
import com.example.product.dto.response.CategoryResponse;
import com.example.product.exception.ResourceNotFoundException;
import com.example.product.repository.CategoryRepository;
import com.example.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultCategoryService implements CategoryService {

  private final CategoryRepository repository;

  @Override
  public CategoryResponse save(CategoryRequest request) {
    Category entity = toEntity(request);
    entity = repository.save(entity);

    return new CategoryResponse(entity);
  }

  @Override
  public CategoryResponse findById(Long id) {
    return repository.findById(id).map(this::toResponse)
      .orElseThrow(() -> new ResourceNotFoundException(String.format("Category with id [%d] is not found", id)));
  }

  private CategoryResponse toResponse(Category category) {
    return new CategoryResponse(category);
  }

  private Category toEntity(CategoryRequest request) {
    Category entity = new Category();
    entity.setName(request.getName());
    List<Category> children = Optional.ofNullable(request.getChildren())
      .orElseGet(Collections::emptyList)
      .stream()
      .map(cr -> {
        Category child = toEntity(cr);
        child.setParent(entity);

        return child;
      })
      .collect(Collectors.toList());
    entity.setChildren(children);


    return entity;
  }

}
