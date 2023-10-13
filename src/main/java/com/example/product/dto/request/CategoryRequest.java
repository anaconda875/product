package com.example.product.dto.request;

import com.example.product.annotation.Unique;
import com.example.product.dto.UniqueIdenfiable;
import com.example.product.service.impl.DefaultCategoryService;
import java.util.List;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@Unique(service = DefaultCategoryService.class)
public class CategoryRequest implements UniqueIdenfiable<String> {

  private String name;

  @Size(min = 0, max = 10)
  private List<CategoryRequest> children;

  @Override
  public String getId() {
    return name;
  }
}
