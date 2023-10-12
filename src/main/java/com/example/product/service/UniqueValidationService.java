package com.example.product.service;

import java.util.List;

public interface UniqueValidationService<ID> {

  //todo: we need multiple field here
  List<String> findInvalidFields(ID id);
}
