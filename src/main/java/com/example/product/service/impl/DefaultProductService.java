package com.example.product.service.impl;

import com.example.product.domain.model.Category;
import com.example.product.domain.model.Product;
import com.example.product.dto.request.CheckoutRequest;
import com.example.product.dto.request.ProductRequest;
import com.example.product.dto.response.ProductResponse;
import com.example.product.exception.BadRequestException;
import com.example.product.exception.ResourceNotFoundException;
import com.example.product.repository.CategoryRepository;
import com.example.product.repository.ProductRepository;
import com.example.product.service.ProductService;
import com.example.product.service.UniqueValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DefaultProductService implements ProductService, UniqueValidationService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final StreamBridge streamBridge;

    @Override
    public ProductResponse save(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategory().getId())
                .orElseThrow(() -> new BadRequestException("Category not found"));

        Product entity = this.from(request);
        entity.setCategory(category);

        productRepository.save(entity);
        return new ProductResponse(entity);
    }

    @Override
    public void checkout(String accountNumber, List<CheckoutRequest> checkoutRequests) {
        List<Long> ids = checkoutRequests.stream()
          .map(CheckoutRequest::getProductId).collect(Collectors.toList());
        Map<Long, Product> products = productRepository.findAllById(ids).stream()
          .collect(Collectors.toMap(Product::getId, p -> p));

        double total = 0;
        for (int i = 0; i < checkoutRequests.size(); i++) {
            CheckoutRequest req = checkoutRequests.get(i);
            Integer quantity = req.getQuantity();
            Product product = products.get(req.getProductId());
            if(product.getAmount() < quantity) {
                throw new RuntimeException();
            }
            total += (quantity * product.getPrice());
        }

        String cid = UUID.randomUUID().toString();
        System.out.println(total);
        streamBridge.send("pay-out-0", Map.of("accountNumber", accountNumber, "amount", total));
    }

    private Product from(ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .amount(request.getAmount())
                .status(request.getStatus())
                .build();
    }

    @Override
    public List<String> findInvalidFields(Long id, Map<String, Object> fields) {
        String name = (String) fields.get("name");
        int count = productRepository.countByName(name);
        return count > 0 ? List.of(name) : Collections.emptyList();
    }
}
