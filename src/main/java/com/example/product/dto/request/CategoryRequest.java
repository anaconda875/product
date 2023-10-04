package com.example.product.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CategoryRequest {

  private String name;
  private List<CategoryRequest> children;

}
