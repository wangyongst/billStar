package com.tuofan.core;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

@Data
public class PageAndObject {

    private IPage page;

    private Object object;

    public PageAndObject(IPage page, Object object) {
        this.page = page;
        this.object = object;
    }
}
