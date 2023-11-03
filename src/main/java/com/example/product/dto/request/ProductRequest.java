package com.example.product.dto.request;

import com.example.product.annotation.Unique;
import com.example.product.domain.model.Category;
import com.example.product.domain.model.Product;
import com.example.product.dto.UniqueIdentifiable;
import com.example.product.service.impl.DefaultCategoryService;
import com.example.product.service.impl.DefaultProductService;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Unique(service = DefaultProductService.class)
public class ProductRequest implements UniqueIdentifiable {

  private Long id;

  @Length(max = 20)
  private String name;

  @Min(0)
  private Integer price;

  @Min(0)
  private Integer amount;

  @NotNull
  private Product.Status status;

  @NotNull
  private CategoryRequest category;

  @Override
  public Long getIdToCheck() {
    return id;
  }

  @Override
  public Map<String, Object> getUniqueFields() {
    return Map.of("name", name);
  }
}
