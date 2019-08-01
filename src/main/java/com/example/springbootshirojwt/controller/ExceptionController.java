package com.example.springbootshirojwt.controller;

import com.example.springbootshirojwt.util.R;
import com.example.springbootshirojwt.util.UnauthorizedException;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
//restful要统一返回的格式，所以我们也要全局处理Spring Boot的抛出异常。利用@RestControllerAdvice能很好的实现,但只能捕捉controller层抛出的异常,
// 但是如果一个在内层的错误(mapper,service层等)若果一只没有被捕捉，等抛到controller时也能被捕捉。
@RestControllerAdvice
public class ExceptionController {
    // 捕捉UnauthorizedException
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public R handleError(UnauthorizedException e) {
        e.printStackTrace();
        return new R(401,"Unauthorized" , null);
    }
    // 捕捉其他所有异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R globalException(HttpServletRequest request, Throwable ex) {
        System.out.println("ExceptionController.globalException()");
        ex.printStackTrace();
        return new R(getStatus(request).value(), ex.getMessage(), null);
    }
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}

