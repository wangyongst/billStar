package com.tuofan.biz.sys_ding.request.param.robot.messsage;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuofan.biz.sys_ding.request.param.msg.Message;
import lombok.Data;

@Data
public class MarkDownMessageDTO {

    @JsonProperty(value = "msgtype")
    String msgType;

    MessageDTO markdown;

    public static MarkDownMessageDTO of(String title, String text) {
        MarkDownMessageDTO markDownMessageDTO = new MarkDownMessageDTO();
        markDownMessageDTO.setMsgType("markdown");
        MessageDTO messageDTO = MessageDTO.of(title, text);
        markDownMessageDTO.setMarkdown(messageDTO);
        return markDownMessageDTO;
    }
}
