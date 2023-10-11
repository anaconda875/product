package com.example.product.web.support;

import com.example.product.dto.request.CustomPageable;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CustomPageableHandlerMethodArgumentResolver
    extends PageableHandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter methodParameter) {
    return CustomPageable.class.equals(methodParameter.getParameterType());
  }

  @Override
  public Pageable resolveArgument(
      MethodParameter methodParameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    Pageable pageable =
        super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
    String kw = webRequest.getParameter(getParameterNameToUse("kw", methodParameter));
    return new CustomPageable(pageable, kw);
  }
}
