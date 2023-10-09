package com.example.product.repository;

import com.example.product.domain.model.Category;
import com.example.product.dto.projection.CategoryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  int countByName(String name);

  @Override
  @EntityGraph(value = "test", type = EntityGraph.EntityGraphType.LOAD)
  Optional<Category> findById(Long id);

  @Query(value = "WITH RECURSIVE cte AS ( " +
                    "SELECT a.id, a.parent_id, a.name " +
                    "FROM tbl_category a WHERE a.id = ?1 " +
                  "UNION ALL " +
                    "SELECT t.id, t.parent_id, t.name " +
                    "FROM tbl_category t " +
                    "INNER JOIN cte c ON t.parent_id = c.id " +
                    ") " +
                  "SELECT parent_id as parentId, id, name FROM cte " +
                  "GROUP BY id, parentId " +
                  "ORDER BY parent_id"

  , nativeQuery = true)
  List<CategoryProjection> findByIdIncludeChildrenRecursively(Long id);

  @Query("SELECT c FROM Category c WHERE ?1 IS NULL OR c.name LIKE %?1%")
  Page<Category> findAll(String kw, Pageable pageable);
}
