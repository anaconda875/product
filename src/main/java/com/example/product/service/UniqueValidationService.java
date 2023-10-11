package com.example.product.service;

import java.util.List;

public interface UniqueValidationService<ID> {

  List<String> findInvalidFields(ID id);
}
