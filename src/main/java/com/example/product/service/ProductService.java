package com.example.product.service;

import com.example.product.dto.request.ProductRequest;
import com.example.product.dto.response.ProductResponse;

public interface ProductService {

    ProductResponse save(ProductRequest request);

}
