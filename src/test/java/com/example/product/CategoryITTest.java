package com.example.product;

import com.example.product.domain.CustomPageImpl;
import com.example.product.domain.model.Category;
import com.example.product.dto.request.CategoryRequest;
import com.example.product.dto.response.CategoryResponse;
import com.example.product.repository.CategoryRepository;
import com.example.product.service.CategoryService;
import com.example.product.service.impl.DefaultCategoryService;
import com.example.product.validation.UniqueValidator;
import com.example.product.web.rest.CategoryResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)

//@Testcontainers
@WebMvcTest(value = {CategoryResource.class/*, CategoryRepository.class*//*, CategoryService.class*/})
class CategoryITTest {

  @Autowired protected WebApplicationContext wac;
  @MockBean private DefaultCategoryService categoryService;
  @Autowired private FilterChainProxy springSecurityFilterChain;

  protected MockMvc mockMvc;

  @Test
  void create_withValidJson_shouldCreateAnDbEntry() throws Exception {
    CategoryRequest request = new CategoryRequest();
    request.setName("cate 1");
    CategoryRequest child = new CategoryRequest();
    child.setName("cate 2");
    request.setChildren(List.of(child));

    Mockito.when(categoryService.save(Mockito.any(CategoryRequest.class))).thenAnswer(invocation -> {
      CategoryRequest rq = invocation.getArgument(0);
      CategoryResponse response = new CategoryResponse();
      BeanUtils.copyProperties(rq, response);

      response.setId(1L);
      return response;
    });

    mockMvc = webAppContextSetup(wac)
      .defaultRequest(post("/categories")
        .with(user("user").password("password").roles("USER")))
      .apply(springSecurity(springSecurityFilterChain))
      .build();
    mockMvc.perform(post("/categories")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(new ObjectMapper().writeValueAsString(request))
      .with(csrf()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
      .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("cate 1")));
  }

  @Test
  void findById_withValidId_shouldReturnEntry() {
    Category entity = new Category();
    entity.setName("111");
    Category child = new Category();
    child.setName("222");
    entity.setChildren(List.of(child));
//    Category saved = categoryRepository.save(entity);

//    CategoryResponse response =
//        testRestTemplate.getForObject("/categories/{id}", CategoryResponse.class, saved.getId());
//    Assertions.assertThat(response.getName()).isEqualTo(saved.getName());
//
//    response =
//        testRestTemplate.getForObject(
//            "/categories/{id}", CategoryResponse.class, saved.getChildren().get(0).getId());
//    Assertions.assertThat(response.getName()).isEqualTo(saved.getChildren().get(0).getName());
  }

  @Test
  @SuppressWarnings("unchecked")
  void search_withExistedName_shouldReturnEntry() {
    Category entity = new Category();
    entity.setName("111");
    Category child = new Category();
    child.setName("222");
    entity.setChildren(List.of(child));
//    Category saved = categoryRepository.save(entity);

//    Page<Map<String, Object>> page =
//        testRestTemplate.getForObject(
//            "/categories?page={p}&size={s}&kw={k}", CustomPageImpl.class, 0, 5, "11");
//    Assertions.assertThat(page.getTotalElements()).isEqualTo(1);
//    Map<String, Object> map = page.getContent().get(0);
//    CategoryResponse categoryResponse = objectMapper.convertValue(map, CategoryResponse.class);
//    Assertions.assertThat(categoryResponse.getName()).isEqualTo(saved.getName());
  }

  @AfterEach
  public void cleanup() {
//    categoryRepository.deleteAll();
  }
}
