package com.tuofan.ding.request.param;

import lombok.Data;

@Data
public class BaseRequestBody {

    public Integer size;

    public Integer offset;

    public BaseRequestBody(int size, int offset) {
        this.size = size;
        this.offset = offset;
    }

    public BaseRequestBody() {
        
    }
}
