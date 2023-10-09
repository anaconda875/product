package com.example.product.dto.request;

import com.example.product.annotation.Unique;
import com.example.product.dto.UniqueIdenfiable;
import com.example.product.service.impl.DefaultCategoryService;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Unique(service = DefaultCategoryService.class)
public class CategoryRequest implements UniqueIdenfiable<String> {

  @NotBlank
  private String name;

  @Size(min = 0, max = 10)
  private List<CategoryRequest> children;

  @Override
  public String getId() {
    return name;
  }
}
