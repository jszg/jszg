package com.xtuer.controller;

import com.xtuer.bean.Result;
import com.xtuer.constant.UriView;
import com.xtuer.util.CommonUtils;
import com.xtuer.util.ImageZipUtil;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
        logger.warn("上传图片结束");
        return Result.ok(tempName);
    }

    public boolean extensionAllowed(String name) {
        String ext = FilenameUtils.getExtension(name).toLowerCase();
        return ext.endsWith("jpg") || ext.endsWith("jpeg");
    }

    @PostMapping(UriView.URI_PICTURE_SUBMIT)
    @ResponseBody
    public Result<String> saveFile(HttpServletRequest request, @RequestParam String photo, @RequestParam Long regId) throws IOException {
        String tempDir = config.getString("uploadTemp"); // 图片的临时目录
        String tempPhotoPath = tempDir + File.separator + photo; // 临时图片路径
        String photoDir = config.getString("uploadRegPhotoDir"); // 图片的最终目录
        String photoPath = generateRequestPhotoPath(regId, photoDir);
        try {
            File file=new File(photoPath);
            if(file.exists()){
                FileUtils.deleteQuietly(new File(photoPath));
            }
            FileUtils.moveFile(new File(tempPhotoPath), new File(photoPath));
        } catch (IOException e) {
            logger.warn("移动图片失败: {}", e.getMessage());
        }
        try {
            FileUtils.deleteQuietly(new File(tempPhotoPath));
        } catch (Exception e) {
            logger.warn("临时目录图片删除失败: {}", e.getMessage());
        }
        return Result.ok("ok");
    }

    /**
     * 根据 enrollId 创建注册使用的图片路径
     * 初始文件名为 enrollId 补齐 0 到 10 字符加图片后缀
     * 保存路径: 0000001234.jpg to 00/0001/2345.jpg
     *
     * @param regId
     * @return 图片路径
     */
    public String generateRequestPhotoPath(long regId, String dir) {
        String photoName = String.format("%010d.jpg", regId);
        String photoPath = dir + File.separator + photoName.substring(0,2) + File.separator +
                photoName.substring(2,6) + File.separator + photoName.substring(6);
        return photoPath;
    }

}
