package com.example.product.web.rest;

import com.example.product.dto.request.CategoryRequest;
import com.example.product.dto.response.CategoryResponse;
import com.example.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryResource {

  private final CategoryService service;

  @PostMapping
  public CategoryResponse save(@RequestBody CategoryRequest categoryRequest) {
    return service.save(categoryRequest);
  }

  @GetMapping("/{id}")
  public CategoryResponse findById(@PathVariable Long id) {
    return service.findById(id);
  }
}
