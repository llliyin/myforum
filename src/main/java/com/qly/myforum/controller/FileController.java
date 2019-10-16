package com.qly.myforum.controller;

import com.qly.myforum.dto.FileDTO;
import com.qly.myforum.provider.UCloudProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@Slf4j
public class FileController {
    @Autowired
    UCloudProvider ucloudProvider;

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request){
        System.out.println("进入图片上传controller");
        MultipartHttpServletRequest multipartHttpServletRequest =(MultipartHttpServletRequest) request;
        MultipartFile file = multipartHttpServletRequest.getFile("editormd-image-file");
        try {
            String url = ucloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setMessage("成功");
            fileDTO.setUrl(url);
            return fileDTO;
        } catch (IOException e) {
            log.error("uploadController error , {}" ,e);
           FileDTO fileDTO= new FileDTO();
           fileDTO.setMessage("上传失败");
           fileDTO.setSuccess(0);
           fileDTO.setUrl(null);
           return  fileDTO;

        }

    }
}
