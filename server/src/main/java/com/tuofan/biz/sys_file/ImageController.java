package com.tuofan.biz.sys_file;

import com.google.common.collect.Lists;
import com.tuofan.biz.sys_file.service.ImageStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("${cert.api.prefix}/image")
public class ImageController {


    @Value("${file.image.suffix}")
    private List<String> imageFileSuffix;

    @Autowired
    private ImageStoreService imageStoreService;

    @PostMapping("uploadList")
    public List<String> upload(@RequestParam MultipartFile[] files, @RequestParam String group) throws IOException {
        List<String> list = Lists.newArrayList();
        for (MultipartFile file : files) {
            list.add(imageStoreService.save(file, group));
        }
        return list;
    }

    @PostMapping("upload")
    public String upload(@RequestParam MultipartFile file, @RequestParam String group) throws IOException {
        return imageStoreService.save(file, group);
    }
}
