package com.tuofan.ding.service;


import com.tuofan.core.BizException;
import com.tuofan.configs.constants.ConfigNameConstants;
import com.tuofan.configs.entity.SysConfigs;
import com.tuofan.configs.service.ConfigCachedUtils;
import com.tuofan.ding.request.TicketRequest;
import com.tuofan.ding.response.TicketResponse;
import com.tuofan.ding.response.base.BaseResponse;
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

    private boolean isTicketExpired(SysConfigs config) {
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
        SysConfigs config = configCachedUtils.getItem(ConfigNameConstants.jsApiTicket);
        if (config == null) {
            String token = obtainTicketFromRemote();
            config = new SysConfigs();
            config.setName(ConfigNameConstants.jsApiTicket);
            config.setValue(token);
            config.setUpdateDate(new Date());
            configCachedUtils.saveOrUpdate(config);
        }
        if (this.isTicketExpired(config)) {
            String token = obtainTicketFromRemote();
            config.setValue(token);
            configCachedUtils.saveOrUpdate(config);
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
