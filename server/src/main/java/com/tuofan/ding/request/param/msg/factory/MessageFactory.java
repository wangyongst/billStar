package com.tuofan.ding.request.param.msg.factory;

import com.tuofan.ding.request.param.msg.LinkMsg;
import com.tuofan.ding.request.param.msg.Message;
import com.tuofan.ding.request.param.msg.constants.MsgTypeConstants;
import org.springframework.stereotype.Component;

/**
 * 消息工厂类
 */
@Component
public class MessageFactory {

    public Message createLinkMessage(String title, String messageUrl, String picUrl, String text) {
        Message message = new Message(MsgTypeConstants.typeLink);
        LinkMsg linkMsg = new LinkMsg();
        linkMsg.setTitle(title);
        linkMsg.setText(text);
        linkMsg.setMessageUrl(messageUrl);
        linkMsg.setPicUrl(picUrl);
        message.setLink(linkMsg);
        return message;
    }
}
