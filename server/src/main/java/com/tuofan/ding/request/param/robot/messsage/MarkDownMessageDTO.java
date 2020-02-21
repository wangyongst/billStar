package com.tuofan.ding.request.param.robot.messsage;

import com.fasterxml.jackson.annotation.JsonProperty;
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
