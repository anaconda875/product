package com.example.product.dto.request;

import com.example.product.annotation.Unique;
import com.example.product.dto.UniqueIdentifiable;
import com.example.product.service.impl.DefaultCategoryService;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@Unique(service = DefaultCategoryService.class)
public class CategoryRequest implements UniqueIdentifiable {

  private Long id;

  private String name;

  @Size(min = 0, max = 10)
  private List<CategoryRequest> children;

  @Override
  public Long getIdToCheck() {
    return id;
  }

  @Override
  public Map<String, Object> getUniqueFields() {
    return Map.of("name", name);
  }

}
