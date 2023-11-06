package com.example.product;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.example.product.domain.CustomPageImpl;
import com.example.product.dto.request.CategoryRequest;
import com.example.product.dto.request.CustomPageable;
import com.example.product.dto.response.CategoryResponse;
import com.example.product.service.impl.DefaultCategoryService;
import com.example.product.web.rest.CategoryResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = {CategoryResource.class})
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

    Mockito.when(categoryService.save(Mockito.any(CategoryRequest.class)))
        .thenAnswer(
            invocation -> {
              CategoryRequest rq = invocation.getArgument(0);
              CategoryResponse response = new CategoryResponse();
              BeanUtils.copyProperties(rq, response);

              response.setId(1L);
              return response;
            });

    mockMvc =
        webAppContextSetup(wac)
            .defaultRequest(
                post("/categories").with(user("user").password("password").roles("USER")))
            .apply(springSecurity(springSecurityFilterChain))
            .build();
    mockMvc
        .perform(
            post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(request))
                .with(csrf()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("cate 1")));
  }

  @Test
  void findById_withValidId_shouldReturnEntry() throws Exception {
    CategoryResponse response = new CategoryResponse();
    response.setId(1L);
    response.setName("111");
    CategoryResponse child = new CategoryResponse();
    child.setName("222");
    response.setChildren(List.of(child));

    Mockito.when(categoryService.findById(1L)).thenReturn(response);
    mockMvc =
        webAppContextSetup(wac)
            .defaultRequest(
                MockMvcRequestBuilders.get("/categories/1")
                    .with(user("user").password("password").roles("USER")))
            .apply(springSecurity(springSecurityFilterChain))
            .build();

    mockMvc
        .perform(MockMvcRequestBuilders.get("/categories/1").with(csrf()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("111")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.children[0].name", Matchers.is("222")));
  }

  @Test
  void search_withExistedName_shouldReturnEntry() throws Exception {
    CategoryResponse response = new CategoryResponse();
    response.setId(1L);
    response.setName("111");
    CategoryResponse child = new CategoryResponse();
    child.setName("222");
    response.setChildren(List.of(child));

    CustomPageImpl<CategoryResponse> page = new CustomPageImpl<>();
    page.setContent(List.of(response));
    Mockito.when(categoryService.findAll(Mockito.any(CustomPageable.class))).thenReturn(page);

    mockMvc =
        webAppContextSetup(wac)
            .defaultRequest(
                MockMvcRequestBuilders.get("/categories")
                    .with(user("user").password("password").roles("USER")))
            .apply(springSecurity(springSecurityFilterChain))
            .build();

    mockMvc
        .perform(MockMvcRequestBuilders.get("/categories").with(csrf()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", Matchers.is(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", Matchers.is("111")))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.content[0].children[0].name", Matchers.is("222")));
  }
}
