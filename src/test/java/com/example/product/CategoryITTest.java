package com.example.product;

import com.example.product.domain.CustomPageImpl;
import com.example.product.domain.model.Category;
import com.example.product.dto.request.CategoryRequest;
import com.example.product.dto.response.CategoryResponse;
import com.example.product.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Testcontainers
class CategoryITTest {

  @Autowired private TestRestTemplate testRestTemplate;
  @Autowired private CategoryRepository categoryRepository;
  @Autowired private ObjectMapper objectMapper;

  @Container
  private static final MariaDBContainer<?> DB_CONTAINER =
      new MariaDBContainer<>("mariadb:10.6.13-focal")
          .withDatabaseName("testcontainer")
          .withUsername("test")
          .withPassword("test");

  @DynamicPropertySource
  static void initEnvs(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.username", DB_CONTAINER::getUsername);
    registry.add("spring.datasource.password", DB_CONTAINER::getPassword);
    registry.add("spring.datasource.url", DB_CONTAINER::getJdbcUrl);
  }

  @BeforeEach
  public void init() {
    testRestTemplate.getRestTemplate().setErrorHandler(new DefaultResponseErrorHandler());
  }

  @Test
  void create_withValidJson_shouldCreateAnDbEntry() {
    CategoryRequest request = new CategoryRequest();
    request.setName("cate 1");
    CategoryRequest child = new CategoryRequest();
    child.setName("cate 2");
    request.setChildren(List.of(child));

    CategoryResponse response =
        testRestTemplate.postForObject("/categories", request, CategoryResponse.class);

    Assertions.assertThat(response.getName()).isEqualTo(request.getName());
    Assertions.assertThat(response.getChildren().size()).isEqualTo(1);
    Assertions.assertThat(response.getChildren().get(0).getName()).isEqualTo(child.getName());
  }

  @Test
  void findById_withValidId_shouldReturnEntry() {
    Category entity = new Category();
    entity.setName("111");
    Category child = new Category();
    child.setName("222");
    entity.setChildren(List.of(child));
    Category saved = categoryRepository.save(entity);

    CategoryResponse response =
        testRestTemplate.getForObject("/categories/{id}", CategoryResponse.class, saved.getId());
    Assertions.assertThat(response.getName()).isEqualTo(saved.getName());

    response =
        testRestTemplate.getForObject(
            "/categories/{id}", CategoryResponse.class, saved.getChildren().get(0).getId());
    Assertions.assertThat(response.getName()).isEqualTo(saved.getChildren().get(0).getName());
  }

  @Test
  @SuppressWarnings("unchecked")
  void search_withExistedName_shouldReturnEntry() {
    Category entity = new Category();
    entity.setName("111");
    Category child = new Category();
    child.setName("222");
    entity.setChildren(List.of(child));
    Category saved = categoryRepository.save(entity);

    Page<Map<String, Object>> page =
        testRestTemplate.getForObject(
            "/categories?page={p}&size={s}&kw={k}", CustomPageImpl.class, 0, 5, "11");
    Assertions.assertThat(page.getTotalElements()).isEqualTo(1);
    Map<String, Object> map = page.getContent().get(0);
    CategoryResponse categoryResponse = objectMapper.convertValue(map, CategoryResponse.class);
    Assertions.assertThat(categoryResponse.getName()).isEqualTo(saved.getName());
  }

  @AfterEach
  public void cleanup() {
    categoryRepository.deleteAll();
  }
}
