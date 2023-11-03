package com.example.product.service;

import java.util.List;
import java.util.Map;

public interface UniqueValidationService {

  List<String> findInvalidFields(Long id, Map<String, Object> fields);

}
