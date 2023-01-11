package com.back.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHander {

    @ExceptionHandler(value = Exception.class)
    public Result handler(Exception e){
        log.info("发生异常：{},{}",e.toString(),e.getStackTrace().toString());
        return Result.fail();
    }
}
