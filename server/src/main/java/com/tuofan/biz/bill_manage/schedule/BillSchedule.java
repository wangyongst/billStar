package com.tuofan.biz.bill_manage.schedule;

import com.tuofan.biz.bill_manage.component.BillNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BillSchedule {

    @Autowired
    private BillNoticeService billNoticeService;

    /**
     * 每天8点，发送营收数据的通知
     */
    @Scheduled(cron = "0 0 20 * * ?")
    public void sendTotalReportNotice() {
        log.info("sendTotalReportNotice");
        try {
            billNoticeService.triggerNotice();
        } catch (Exception e) {
            log.error("sendTotalReportNotice error = ", e);
        }

    }
}
