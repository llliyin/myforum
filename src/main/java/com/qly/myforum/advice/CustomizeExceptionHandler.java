package com.qly.myforum.advice;

import com.alibaba.fastjson.JSON;
import com.qly.myforum.dto.ResultDTO;
import com.qly.myforum.exception.CustomizeErrorCode;
import com.qly.myforum.exception.CustomizeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
@Slf4j
public class CustomizeExceptionHandler {

    //无法同时返回ModelAndView，使用流的方式向前台传送被格式化成json的字符串
     @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Throwable ex , Model model){
            String contentType=request.getContentType();

//            要是请求是一个application/json请求，就返回result
            if("application/json".equals(contentType)){
                ResultDTO resultDTO;
                if(ex instanceof CustomizeException){
                    resultDTO=ResultDTO.errorOf((CustomizeException)ex);
                }else {
                    log.error("handle error",ex);
                    resultDTO=ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
                }

                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("UTF-8");
                try {
                    PrintWriter writer = response.getWriter();
                    writer.write(JSON.toJSONString(resultDTO));
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }else {
//                要是请求类型不是applicatiion请求，就返回一个页面
                if(ex instanceof CustomizeException){
                    model.addAttribute("message",ex.getMessage());
                }else {
                    log.error("handle error",ex);
                    model.addAttribute("message", CustomizeErrorCode.SYS_ERROR);
                }
            }
            return new ModelAndView("error");
        }


}
