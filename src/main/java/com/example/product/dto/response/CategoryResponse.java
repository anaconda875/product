package com.example.product.dto.response;

import com.example.product.domain.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

  private Long id;
  private String name;
  private List<CategoryResponse> children;

  public CategoryResponse(Category entity) {
    this.id = entity.getId();
    this.name = entity.getName();
    this.children = Optional.ofNullable(entity.getChildren())
      .orElseGet(Collections::emptyList)
      .stream()
      .map(CategoryResponse::new).collect(Collectors.toList());
  }
}
