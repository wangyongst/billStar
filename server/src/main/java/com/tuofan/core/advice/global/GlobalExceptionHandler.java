package com.tuofan.core.advice.global;


import com.tuofan.core.dto.BaseResult;
import com.tuofan.core.exception.BizException;
import com.tuofan.core.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.DecodeException;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Description: 全局异常处理
 *
 * @author yuanma
 * @version 1.0
 * @date 2018/5/15 15:01
 * @since JDK 1.8
 */
@ControllerAdvice(annotations = {RestController.class})
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleUnCatchException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("Controller未处理的异常：url=" + request.getRequestURL(), e);
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            // feign 自定义decode 抛出的异常取出上层的ServiceException
            if (e instanceof DecodeException) {
                Throwable cause = e.getCause();
                if (cause instanceof BizException) {
                    BizException serviceException = BizException.class.cast(cause);
                    handleErrorResult(request, response, serviceException.getCode(), serviceException.getMsg());
                    return null;
                }
            }

            if (e instanceof BizException) {
                BizException serviceException = BizException.class.cast(e);
                handleErrorResult(request, response, serviceException.getCode(), serviceException.getMsg());
                return null;
            }

            // 参数校验错误
            if (e instanceof MethodArgumentNotValidException) {
                MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
                String error = exception.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("|"));
                BizException bizException = new BizException("paramError", error);
                handleErrorResult(request, response, bizException.getCode(), bizException.getMsg());
                return null;
            }
            if(e instanceof NullPointerException){
                BizException bizException = new BizException(e);
                bizException.setCode("23456");
                bizException.setMsg("空指针，位置"+e.getStackTrace()[0].toString());
                handleErrorResult(request, response, bizException.getCode(), bizException.getMsg());
                return null;
            }
            BizException serviceException = new BizException(e);
            handleErrorResult(request, response, serviceException.getCode(), serviceException.getMsg());
            return null;
        } catch (IOException ioe) {
            return null;
        }
    }

    /**
     * Description: 处理异常信息，feign调用的错误码放在response header
     *
     * @author yuanma
     * @version 1.0
     * @date 2018/5/15 15:01
     * @since JDK 1.8
     */
    private void handleErrorResult(HttpServletRequest request, HttpServletResponse response, String errorCode,
                                   String errorMessage) throws IOException {

        BaseResult<String> baseResult = new BaseResult<>(errorCode, errorMessage);
        response.getWriter().print(JsonUtils.toJsonString(baseResult));
    }
}
