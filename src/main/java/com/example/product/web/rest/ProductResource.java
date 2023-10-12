package com.example.product.web.rest;

import com.example.product.dto.request.ProductRequest;
import com.example.product.dto.response.ProductResponse;
import com.example.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductResource {

    private final ProductService productService;

    @PostMapping
    public ProductResponse create(@RequestBody @Valid ProductRequest productRequest) {
        return productService.save(productRequest);
    }

}
