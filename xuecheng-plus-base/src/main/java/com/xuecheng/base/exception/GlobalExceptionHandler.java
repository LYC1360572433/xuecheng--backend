package com.xuecheng.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
//@RestControllerAdvice = @ControllerAdvice + @ResponseBody   处理异常的方法返回值会转换为json后再响应给前端
public class GlobalExceptionHandler {
    //对项目的自定义异常类型进行处理
    @ResponseBody//返回json
    @ExceptionHandler(XueChengPlusException.class)//处理系统的自定义异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)//响应状态码
    public RestErrorResponse customException(XueChengPlusException e) {
        //记录异常日志
        log.error("【系统异常】{}",e.getErrMessage(),e);
        //解析出异常信息
        return new RestErrorResponse(e.getErrCode(),e.getErrMessage());
    }

    //对项目的非自定义异常类型进行处理  总的异常，如果没被其它定义好的异常捕捉，就会到这里来
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(Exception e) {
        //记录异常日志
        log.error("【系统异常】{}",e.getMessage(),e);
        //为了不引入spring security依赖，因为一旦引入，所有其它微服务都自动依赖spring security，因为当初让其它微服务都依赖base这个工程
        if(e.getMessage().equals("不允许访问")){
            return new RestErrorResponse("您没有权限操作此功能");
        }


        //解析出异常信息
        return new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());
    }

    //MethodArgumentNotValidException  JSR303校验
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();
        //存储错误信息
        List<String> errors = new ArrayList<>();
        bindingResult.getFieldErrors().stream().forEach(item->{
            errors.add(item.getDefaultMessage());
        });

        //将list中的错误信息拼接起来
        String errMessage = StringUtils.join(errors, ",");

        //记录异常日志
        log.error("【系统异常】{}",e.getMessage(),errMessage);

        //解析出异常信息
        return new RestErrorResponse(errMessage);
    }
}