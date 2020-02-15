package com.tuofan.biz.sys_resource.dto;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class CreateImgCmd {

    private String modelName;

    private Integer modelId;

    private String url;

    public CreateImgCmd(String modelName, Integer modelId, String url) {
        this.modelId = modelId;
        this.modelName = modelName;
        this.url = url;
    }


}
