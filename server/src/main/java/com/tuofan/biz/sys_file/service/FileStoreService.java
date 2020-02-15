package com.tuofan.biz.sys_file.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStoreService {
    /**
     *  保存上传的文件
     * @param multipartFile
     * @param group
     * @return 文件访问url
     */
    String save(MultipartFile multipartFile, String group) throws IOException;

//    String saveBigFile(BigFileUploadParam param, MultipartFile multipartFile, String tenantId, String group) throws IOException;
}
