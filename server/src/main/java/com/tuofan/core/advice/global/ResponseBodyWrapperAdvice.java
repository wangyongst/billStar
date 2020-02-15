package com.tuofan.core.advice.global;

import com.tuofan.core.dto.BaseResult;
import com.tuofan.core.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice(annotations = { RestController.class })
@Slf4j
public class ResponseBodyWrapperAdvice implements ResponseBodyAdvice<Object> {

	private static final String BASE_PACKAGE = "com.tuofan";

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
//		FastJsonHttpMessageConverter.class.isAssignableFrom(converterType);
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {

		if (!returnType.getMethod().getDeclaringClass().getName().startsWith(BASE_PACKAGE)) {
			return body;
		}
		if (body instanceof BaseResult) {
			return body;
		}
		BaseResult<Object> result = BaseResult.buildSuccessResult();
		result.setData(body);
		if(body instanceof String){
			return JsonUtils.toJsonString(result);
		}
		return result;
	}

}
