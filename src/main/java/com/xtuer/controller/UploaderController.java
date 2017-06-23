package com.xtuer.controller;

import com.xtuer.bean.Result;
import com.xtuer.constant.UriView;
import com.xtuer.util.CommonUtils;
import com.xtuer.util.ImageZipUtil;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Resource(name = "config")
    private PropertiesConfiguration config;

    private static Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

    @PostMapping(UriView.URI_UPLOAD_ENROLL_IMAGE)
    @ResponseBody
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        // 把上传的文件保存到临时文件夹，返回唯一的临时文件名，以供稍后从移动到最终目录
        String tempName = CommonUtils.generateUniqueFileName(file.getOriginalFilename());
        logger.warn("上传图片开始,上传图片的名称是==="+tempName);
        // 判断格式是否允许
        if (!extensionAllowed(tempName)) {
            logger.warn("上传图片格式错误");
            return new Result(false, FilenameUtils.getExtension(tempName).toLowerCase() + " 格式的图片不允许上传");
        }

        String tempDir = config.getString("uploadTemp");
        file.transferTo(new File(tempDir + File.separator + tempName));
        //图片压缩
        ImageZipUtil.zipImageFile(new File(tempDir + File.separator + tempName),new File(tempDir + File.separator + tempName),114, 156, 1f);
        logger.warn("上传图片结束");
        return Result.ok(tempName);
    }

    public boolean extensionAllowed(String name) {
        String ext = FilenameUtils.getExtension(name).toLowerCase();
        return ext.endsWith("jpg") || ext.endsWith("jpeg");
    }
}
