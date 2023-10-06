package com.example.product.dto.request;

import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
public class CustomPageable extends PageRequest {

  private final String kw;

  public CustomPageable(Pageable pageable, String kw) {
    this(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort(), kw);
  }

  protected CustomPageable(int page, int size, Sort sort, String kw) {
    super(page, size, sort);
    this.kw = kw;
  }
}
