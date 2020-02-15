package com.tuofan.biz.sys_resource.entity;

import com.tuofan.biz.sys_resource.constants.ResourceTypeConstants;
import com.tuofan.core.persistence.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class Resource extends BaseEntity {

    private String modelName;

    private Integer modelId;

    private String type;

    private String url;

    private Integer createBy;

    @Transient
    private Date updateDate;

    public void setTypeImage(){
        this.type = ResourceTypeConstants.IMG;
    }
}
