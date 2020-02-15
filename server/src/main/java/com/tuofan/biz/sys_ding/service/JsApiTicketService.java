package com.tuofan.biz.sys_ding.service;

import com.tuofan.biz.sys_configs.constants.ConfigNameConstants;
import com.tuofan.biz.sys_configs.entity.SystemConfig;
import com.tuofan.biz.sys_configs.service.ConfigCachedUtils;
import com.tuofan.biz.sys_ding.request.TicketRequest;
import com.tuofan.biz.sys_ding.response.TicketResponse;
import com.tuofan.biz.sys_ding.response.base.BaseResponse;
import com.tuofan.core.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JsApiTicketService {

    @Autowired
    private TicketRequest ticketRequest;

    @Autowired
    ConfigCachedUtils configCachedUtils;

    private boolean isTicketExpired(SystemConfig config) {
        if (config == null) {
            return true;
        }
        // 2H
        if (System.currentTimeMillis() - config.getUpdateDate().getTime() > (2 * 60 * 60 * 1000 - 1000)) {
            return true;
        }
        return false;
    }

    public String getTicket() {
        SystemConfig config = configCachedUtils.getItem(ConfigNameConstants.jsApiTicket);
        if (config == null) {
            String token = obtainTicketFromRemote();
            config = new SystemConfig();
            config.setName(ConfigNameConstants.jsApiTicket);
            config.setValue(token);
            config.setUpdateDate(new Date());
            configCachedUtils.create(config);
        }
        if (this.isTicketExpired(config)) {
            String token = obtainTicketFromRemote();
            config.setValue(token);
            configCachedUtils.update(config);
            log.info("DingDing ticket refreshed");
        }
        return config.getValue();
    }

    private String obtainTicketFromRemote() {
        TicketResponse ticket = ticketRequest.get();
        if (ticket.getErrcode() == BaseResponse.OK) {
            return ticket.getTicket();
        }
        throw new BizException("ticket", "obtain ticket error , Code:" + ticket.getErrcode() + ",Msg:" + ticket.getErrmsg());
    }

}
