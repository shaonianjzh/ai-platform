package com.shaonian.project.util;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.shaonian.project.common.ErrorCode;
import com.shaonian.project.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author 少年
 */
@Component
public class FileUtil {


    @Value("${img.src}")
    private String src;

    @Value("${img.url}")
    private String url;

    public String upload(MultipartFile file){
        try {
            long id = IdWorker.getId();
            file.transferTo(new File(src+id+file.getOriginalFilename()));
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"图片上传失败");
        }
        return url+file.getOriginalFilename();
    }
}
