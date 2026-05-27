package com.longan.common.controller;

import com.longan.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/common")
@Slf4j
public class CommonController {

    @Value("${longan.upload.path}") // /var/www/pic_bed
    private String basePath;

    @Value("${longan.upload.domain}") // http://localhost:8888/longan_apex/
    private String domain;

    @PostMapping("/upload/{type}")
    public Result<String> upload(MultipartFile file, @PathVariable String type) throws IOException {
        if (file == null || file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }

        // 1. 校验业务类型，防止别人瞎传路径
        if (!"user".equals(type) && !"goods".equals(type)) {
            return Result.error("非法的上传类型");
        }

        // 2. 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String ext = (originalFilename != null && originalFilename.contains("."))
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : ".png";
        String fileName = UUID.randomUUID().toString() + ext;

        File dir = new File(basePath + File.separator + type + File.separator);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 4. 物理保存
        File dest = new File(dir, fileName);
        file.transferTo(dest);
        log.info("{} 图片已存入: {}", type, dest.getAbsolutePath());

        // 5. 返回访问 URL
        String fullUrl = domain + type + "/" + fileName;
        return Result.success(fullUrl);
    }
}