package com.tuofan.biz.sys_ding.response.base;

import lombok.Data;

import java.util.List;

@Data
public class HasMosResponse<T> {

    private boolean hasMore = false;

    private List<T> list;
}
