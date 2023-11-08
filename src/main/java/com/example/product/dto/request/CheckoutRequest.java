package com.example.product.dto.request;

import lombok.Data;

@Data
public class CheckoutRequest {

  private Long productId;
  private Integer quantity;

}
