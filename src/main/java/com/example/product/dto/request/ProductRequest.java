package com.example.product.dto.request;

import com.example.product.domain.model.Category;
import com.example.product.domain.model.Product;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ProductRequest {

  @Length(max = 20)
  private String name;

  @Min(0)
  private Integer price;

  @Min(0)
  private Integer amount;

  private Product.Status status;

  @NotNull
  private CategoryRequest category;

}
