package com.example.product.dto.request;

import com.example.product.domain.model.Product;
import lombok.Data;

@Data
public class ProductRequest {

  private String name;
  private Integer price;
  private Integer amount;
  private Product.Status status;

}
