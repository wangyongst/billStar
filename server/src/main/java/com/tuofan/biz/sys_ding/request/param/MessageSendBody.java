package com.tuofan.biz.sys_ding.request.param;

import com.tuofan.biz.sys_ding.request.param.msg.Message;
import lombok.Data;

@Data
public class MessageSendBody {

    private Integer agent_id;

    private String userid_list;

    private String dept_id_list;

    private boolean toAllUser;

    private Message msg;

    public MessageSendBody(Message message) {
        this.msg = message;
    }
}
