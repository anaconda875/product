package com.example.product.dto.request;

import com.example.product.annotation.Unique;
import com.example.product.dto.UniqueIdentifiable;
import com.example.product.service.impl.DefaultCategoryService;
import java.util.List;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Unique(service = DefaultCategoryService.class)
public class CategoryRequest implements UniqueIdentifiable<String> {

  private Long id;

  @Length(max = 1)
  private String name;

  @Size(min = 0, max = 10)
  private List<CategoryRequest> children;

  @Override
  public String getUniqueField() {
    return name;
  }
}
