package com.silverteam.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    String permittedSizeException(Exception e) {
        e.printStackTrace();
        return "<h1>The file exceeds its maximum permitted size of 5120KB.</h1>";
    }

}
