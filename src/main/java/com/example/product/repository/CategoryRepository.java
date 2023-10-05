package com.example.product.repository;

import com.example.product.domain.model.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.EntityManager;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

//  @EntityGraph(value = "test", type = EntityGraph.EntityGraphType.FETCH)
  @Query("SELECT DISTINCT c FROM Category c JOIN FETCH c.children")
  Optional<Category> findById(Long id);

}
