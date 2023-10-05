package com.example.product.domain.model;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tbl_category")
@Data
@NamedEntityGraph(
  name = "test",
  attributeNodes = {
    @NamedAttributeNode(value="children",subgraph="children"),
  },
  subgraphs = {
    @NamedSubgraph(
      name = "children",
      attributeNodes = {
        @NamedAttributeNode("children")
      }
    )
  })
@BatchSize(size = 10)
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
  @ToString.Exclude
  @BatchSize(size = 10)
  private List<Category> children;

  @OneToMany(mappedBy = "category")
  @ToString.Exclude
  private List<Product> products;

}
