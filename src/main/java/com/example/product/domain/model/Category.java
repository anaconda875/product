package com.example.product.domain.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tbl_category")
@Data
public class Category {

  @Id
  @GeneratedValue
  private Long id;

  @Basic(optional = false)
  private String name;

  @ManyToOne
  @ToString.Exclude
  private Category parent;

  @OneToMany(mappedBy = "parent", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  private List<Category> children;

  @OneToMany(mappedBy = "category")
  private List<Product> products;

}
