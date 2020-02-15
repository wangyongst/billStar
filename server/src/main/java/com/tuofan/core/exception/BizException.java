package com.tuofan.core.exception;

import com.tuofan.core.utils.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BizException extends RuntimeException {

    private String code;

    private String msg;

    public BizException(String code, String message) {
        this.code = code;
        this.msg = message;
    }

    public BizException(Exception e) {
        super(e);
        if (StringUtils.isEmpty(this.getCode())) {
            this.setCode("5001");
        }
        if (StringUtils.isEmpty(this.getMsg())) {
            this.setMsg("Cause=" + e.getCause() + "Message=" + e.getMessage());
        }
    }

    public static BizException locateByIdException() {
        return new BizException("661", "无法根据ID主键定位数据");
    }

    public static BizException deleteFailForRefereed() {
        return new BizException("662", "已有业务数据关联，不允许删除");
    }
}
