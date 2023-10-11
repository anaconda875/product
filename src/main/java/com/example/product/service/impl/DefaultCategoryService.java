package com.example.product.service.impl;

import com.example.product.domain.model.Category;
import com.example.product.dto.projection.CategoryProjection;
import com.example.product.dto.request.CategoryRequest;
import com.example.product.dto.request.CustomPageable;
import com.example.product.dto.response.CategoryResponse;
import com.example.product.exception.ResourceNotFoundException;
import com.example.product.repository.CategoryRepository;
import com.example.product.service.CategoryService;
import com.example.product.service.UniqueValidationService;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class DefaultCategoryService implements CategoryService, UniqueValidationService<String> {

  private final CategoryRepository repository;

  @Override
  public CategoryResponse save(CategoryRequest request) {
    Category entity = toEntity(request);
    entity = repository.save(entity);

    return new CategoryResponse(entity);
  }

  @Override
  public CategoryResponse findById(Long id) {
    List<CategoryProjection> projections = repository.findByIdIncludeChildrenRecursively(id);
    if (CollectionUtils.isEmpty(projections)) {
      throw new ResourceNotFoundException(String.format("Category with id [%d] is not found", id));
    }

    Map<Long, List<CategoryProjection>> parentMap = new LinkedHashMap<>();
    for (CategoryProjection p : projections) {
      Long parentId = p.getParentId();
      List<CategoryProjection> tmp;
      if (parentMap.containsKey(parentId)) {
        tmp = parentMap.get(parentId);
      } else {
        tmp = new LinkedList<>();
        parentMap.put(parentId, tmp);
      }
      tmp.add(p);
    }

    CategoryProjection first = projections.get(0);

    CategoryResponse result =
        new CategoryResponse(
            first.getId(), first.getName(), buildChildren(first.getId(), parentMap));

    return result;
    //    return repository.findById(id).map(this::toResponse)
    //      .orElseThrow(() -> new ResourceNotFoundException(String.format("Category with id [%d] is
    // not found", id)));
  }

  @Override
  public Page<CategoryResponse> findAll(CustomPageable pageable) {
    return repository.findAll(pageable.getKw(), pageable).map(this::toResponse);
  }

  private List<CategoryResponse> buildChildren(
      Long id, Map<Long, List<CategoryProjection>> parentMap) {
    List<CategoryProjection> children =
        Optional.ofNullable(parentMap.get(id)).orElseGet(Collections::emptyList);
    return children.stream()
        .map(
            child ->
                new CategoryResponse(
                    child.getId(), child.getName(), buildChildren(child.getId(), parentMap)))
        .collect(Collectors.toList());
  }

  private CategoryResponse toResponse(Category category) {
    return new CategoryResponse(category);
  }

  private Category toEntity(CategoryRequest request) {
    Category entity = new Category();
    entity.setName(request.getName());
    List<Category> children =
        Optional.ofNullable(request.getChildren()).orElseGet(Collections::emptyList).stream()
            .map(
                cr -> {
                  Category child = toEntity(cr);
                  child.setParent(entity);

                  return child;
                })
            .collect(Collectors.toList());
    entity.setChildren(children);

    return entity;
  }

  @Override
  public List<String> findInvalidFields(String name) {
    int count = repository.countByName(name);
    if (count > 0) {
      return List.of("name");
    }

    return List.of();
  }
}
