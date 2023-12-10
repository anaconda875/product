package com.example.product.domain.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "tbl_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
