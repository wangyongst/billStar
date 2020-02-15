package com.tuofan.biz.sys_ding.request.param.robot.messsage;

import com.tuofan.biz.sys_ding.request.param.msg.Message;
import lombok.Data;

@Data
public class MessageDTO {

    String title;

    String text;

    public static MessageDTO of(String title,String text){
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setText(text);
        messageDTO.setTitle(title);
        return messageDTO;
    }
}
