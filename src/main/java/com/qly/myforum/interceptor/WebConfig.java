package com.qly.myforum.interceptor;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class WebConfig implements WebMvcConfigurer {
//
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//
//        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**")
//        .excludePathPatterns("/")
//        .excludePathPatterns("/static/**")
//        .excludePathPatterns("/login.html","/login","/css/**","/js/**")
//        .excludePathPatterns("https://github.com/login/oauth/authorize","https://github.com/login/oauth/access_token","https://api.github.com/user");
//    }
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        //其他静态资源，与本文关系不大
//
//        //需要配置1：----------- 需要告知系统，这是要被当成静态文件的！
//        //第一个方法设置访问路径前缀，第二个方法设置资源路径
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/").addResourceLocations("https://cdn.jsdelivr.net/npm/jquery@3.2/dist/jquery.min.js");
//    }
}
