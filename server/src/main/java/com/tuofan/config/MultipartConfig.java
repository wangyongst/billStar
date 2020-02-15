package com.tuofan.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * 解决本地上传文件的报错，找不到临时目录
 */
@Configuration
@Slf4j
public class MultipartConfig {

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String location = System.getProperty("user.dir") + "/certTmp";
        File tmpFile = new File(location);
        if (!tmpFile.exists() && tmpFile.mkdirs()) {
            log.info("创建临时目录:{}", location);
        }
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }
}
