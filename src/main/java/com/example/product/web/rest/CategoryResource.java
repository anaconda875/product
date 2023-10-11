package com.example.product.web.rest;

import com.example.product.dto.request.CategoryRequest;
import com.example.product.dto.request.CustomPageable;
import com.example.product.dto.response.CategoryResponse;
import com.example.product.service.CategoryService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryResource {

  private final CategoryService service;

  @PostMapping
  public CategoryResponse save(@Valid @RequestBody CategoryRequest categoryRequest) {
    return service.save(categoryRequest);
  }

  @GetMapping("/{id}")
  public CategoryResponse findById(@PathVariable Long id) {
    return service.findById(id);
  }

  @GetMapping
  public Page<CategoryResponse> findAll(CustomPageable pageable) {
    return service.findAll(pageable);
  }
}
