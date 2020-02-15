package com.tuofan.biz.sys_file.service;

import com.tuofan.core.exception.BizException;
import com.tuofan.core.utils.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ImageStoreService {

    @Value("#{'${file.image.suffix}'.split(',')}")
    private List<String> imageFileSuffix;

    @Autowired
    private FileStoreService fileStoreService;

    public String save(MultipartFile multipartFile, String group) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String suffix = FilenameUtils.getExtension(fileName);
        if (StringUtils.isBlank(suffix)) {
            throw new BizException("PicNameIllegal", "图片文件名不合法,fileName=" + fileName);
        }
        // 判断后缀是否为图片
        if (imageFileSuffix.contains(suffix.trim().toLowerCase())
                || imageFileSuffix.contains(suffix.trim().toUpperCase())) {
            return fileStoreService.save(multipartFile, group);

        } else {
            throw new BizException("FileNameIllegal", "图片文件名不合法,fileName=" + fileName);
        }

    }
}
