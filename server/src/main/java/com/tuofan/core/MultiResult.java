package com.tuofan.core;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

@Data
public class MultiResult {

    private IPage page;

    private List list;

    private Object object;

    public MultiResult(IPage page, Object object) {
        this.page = page;
        this.object = object;
    }

    public MultiResult(List list, Object object) {
        this.list = list;
        this.object = object;
    }
}
