package com.tuofan.biz.bill_manage.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("动态列")
public class DynamicTableColumn {

    private Integer index;

    private String label;

    private String prop;

    public static DynamicTableColumn of(Integer index , String label , String prop){
        DynamicTableColumn ele = new DynamicTableColumn();
        ele.setProp(prop);
        ele.setLabel(label);
        ele.setIndex(index);
        return ele;
    }
}
