package com.xtuer.controller;

import com.xtuer.bean.Result;
import com.xtuer.constant.UriView;
import com.xtuer.util.CommonUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@Controller
public class UploaderController {
    @PostMapping(UriView.URI_UPLOAD_ENROLL_IMAGE)
    @ResponseBody
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        // 把上传的文件保存到临时文件夹，返回唯一的临时文件名，以供稍后从移动到最终目录
        String tempName = CommonUtils.generateUniqueFileName(file.getOriginalFilename());
        String tempDir = config.getString("uploadTemp");
        file.transferTo(new File(tempDir + File.separator + tempName));

        return Result.ok(tempName);
    }

    @Resource(name = "config")
    private PropertiesConfiguration config;
}
