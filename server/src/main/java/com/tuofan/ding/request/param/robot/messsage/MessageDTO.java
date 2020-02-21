package com.tuofan.ding.request.param.robot.messsage;

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
