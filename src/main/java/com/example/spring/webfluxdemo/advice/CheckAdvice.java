package com.example.spring.webfluxdemo.advice;

import com.example.spring.webfluxdemo.exceptions.CheckException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

/**
 * @program: webflux-demo
 * @description: 参数校验异常处理切面
 * @author: 01
 * @create: 2018-10-05 11:16
 **/

@ControllerAdvice
public class CheckAdvice {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity handleBindException(WebExchangeBindException e) {
        return new ResponseEntity<>(toStr(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CheckException.class)
    public ResponseEntity handleCheckException(CheckException e) {
        return new ResponseEntity<>(toStr(e, "用户名不合法 "), HttpStatus.BAD_REQUEST);
    }

    /**
     * 把自定义校验异常转换为字符串
     *
     * @param e   e
     * @param msg msg
     * @return String
     */
    private String toStr(CheckException e, String msg) {
        return msg + e.getFieldName() + ":" + e.getFieldValue();
    }

    /**
     * 把校验异常转换为字符串
     *
     * @param ex ex
     * @return String
     */
    private String toStr(WebExchangeBindException ex) {
        return ex.getFieldErrors().stream()
                .map(e -> e.getField() + ":" + e.getDefaultMessage())
                .reduce("", (s1, s2) -> s1 + "\n" + s2);
    }
}
