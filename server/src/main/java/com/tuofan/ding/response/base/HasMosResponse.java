package com.tuofan.ding.response.base;

import lombok.Data;

import java.util.List;

@Data
public class HasMosResponse<T> {

    private boolean hasMore = false;

    private List<T> list;
}
