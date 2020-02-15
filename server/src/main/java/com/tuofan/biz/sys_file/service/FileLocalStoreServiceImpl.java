package com.tuofan.biz.sys_file.service;


import com.tuofan.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 本地存储
 */
@Slf4j
@ConditionalOnMissingBean(FileStoreService.class)
public class FileLocalStoreServiceImpl extends AbstractFileStoreService {

    //    @Value("${file.image.url}")
    private String url = "";

    @Value("${file.store.path}")
    private String storePath;


    private String getFileDir(String group) {
        String relativeDir = getRelativeDir(group);
        if (storePath.endsWith("/")) {
            return storePath + relativeDir;
        } else {
            return storePath + "/" + relativeDir;
        }
    }


    @Override
    public String save(MultipartFile multipartFile, String group) throws IOException {
        if (group == null) {
            group = "";
        }

        String fileName = createFileNameForStore(multipartFile.getOriginalFilename());
        String realDirStr = getFileDir(group);
        File realDir = new File(realDirStr);

        if (!realDir.exists()) {
            realDir.mkdirs();
        }

//		拼成完整的文件保存路径加文件
        String fullFileName = realDirStr + fileName;
        File file = new File(fullFileName);

        FileCopyUtils.copy(multipartFile.getInputStream(), new FileOutputStream(file));

        return createResult(fileName, group);
    }


    private boolean shouldMerge(File dir, int total) {
        File[] fileArray = dir.listFiles();
        if (fileArray != null) {
            if (fileArray.length == total) {
                return true;
            }
        }
        return false;
    }


    /**
     * @param fileName 文件名
     * @param group    地址分组
     * @return
     */
    private String createResult(String fileName, String group) {
        StringBuilder builder = new StringBuilder().append(url.trim());
        if (!url.endsWith("/") && StringUtils.isNotEmpty(url)) {
            builder.append("/");
        }
        String relativeDir = getRelativeDir(group);
        builder.append(relativeDir).append(fileName).append("\n");
        return builder.toString().trim();
    }
}
