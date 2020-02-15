package com.tuofan.biz.sys_ding.request.param.msg;

import lombok.Data;

/**
 * 链接类型的消息
 */
@Data
public class LinkMsg {

    private String messageUrl;

    private String picUrl;

    private String title;

    private String text;
}
