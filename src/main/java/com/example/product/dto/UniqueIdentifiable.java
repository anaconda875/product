package com.example.product.dto;

import java.util.Map;

public interface UniqueIdentifiable {

  Long getIdToCheck();

  Map<String, Object> getUniqueFields();
}
