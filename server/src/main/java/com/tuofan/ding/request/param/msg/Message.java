package com.tuofan.ding.request.param.msg;

import lombok.Data;

/**
 * 通知消息体
 */
@Data
public class Message {

    private String msgtype;

    private LinkMsg link;


    public Message() {

    }

    public Message(String msgtype) {
        this.msgtype = msgtype;
    }
}
