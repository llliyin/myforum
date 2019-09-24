package com.qly.myforum.advice;

import com.qly.myforum.exception.CustomizeErrorCode;
import com.qly.myforum.exception.CustomizeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@ControllerAdvice
//@Slf4j
public class CustomizeExceptionHandler {
//        @ExceptionHandler(Exception.class)
//    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Throwable ex , Model model){
//            String contentType=request.getContentType();
//            if("application/json".equals(contentType)){
//
//            }else {
//                if(ex instanceof CustomizeException){
//                    model.addAttribute("message",ex.getMessage());
//                }else {
//                    log.error("handle error",ex);
//                    model.addAttribute("message", CustomizeErrorCode.SYS_ERROR);
//                }
//            }
//            return new ModelAndView("error");
//        }


}
