package com.xtuer.controller;

import com.xtuer.bean.Result;
import com.xtuer.constant.UriView;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Controller
public class UploaderController {
    @RequestMapping(UriView.URI_UPLOAD_PERSON_IMAGE)
    @ResponseBody
    public Result uploadFile(@RequestParam(name="file", required=false) MultipartFile file, HttpServletRequest request) throws IOException {
        System.out.println(file.getOriginalFilename());
        String uploadDir = propertiesConfig.getString("uploadPersonImageDir");
        file.transferTo(new File(uploadDir + File.separator + file.getOriginalFilename()));
        return new Result(true, "OK", file.getOriginalFilename());
    }

    @Resource(name = "propertiesConfig")
    private PropertiesConfiguration propertiesConfig;
}
