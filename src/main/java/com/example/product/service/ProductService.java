package com.example.product.service;

import com.example.product.dto.request.CheckoutRequest;
import com.example.product.dto.request.ProductRequest;
import com.example.product.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse save(ProductRequest request);
    void checkout(String accountNumber, List<CheckoutRequest> checkoutRequests);

}
