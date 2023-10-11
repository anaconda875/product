package com.example.product.domain.model;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_product")
@Data
public class Product {

  @Id @GeneratedValue private Long id;

  @Basic(optional = false)
  private String name;

  @Column(nullable = false)
  private Integer price;

  @Column(nullable = false)
  private Integer amount;

  private Status status;

  @ManyToOne private Category category;

  public enum Status {
    HIDDEN,
    SHOW;
  }
}
