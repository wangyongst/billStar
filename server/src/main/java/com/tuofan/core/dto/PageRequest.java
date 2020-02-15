package com.tuofan.core.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageRequest<T> {

    public PageRequest() {

    }

    public PageRequest(PageRequest other) {
        this.pageNo = other.getPageNo();
        this.pageSize = other.getPageSize();
        this.orderBy = other.getOrderBy();
    }

    private T data;
    @ApiModelProperty(value = "页码从1开始")
    protected int pageNo = 1;
    @ApiModelProperty(value = "不传默认20")
    protected int pageSize = 20;

    protected String orderBy;

}
