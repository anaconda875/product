package com.example.product.dto.response;

import com.example.product.domain.model.Category;
import com.example.product.domain.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;

    private String name;

    private Integer price;

    private Integer amount;

    private Product.Status status;

    //todo: add category, maybe don't include the children

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.amount = product.getAmount();
        this.status = product.getStatus();
    }
}
