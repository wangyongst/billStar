package com.tuofan.biz.bill_manage.vo;

import com.tuofan.biz.sys_configs.service.ConfigCachedUtils;
import com.tuofan.core.utils.DateTimeUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
public class SaleTimeQuery {

    private Date beginDate;

    private Date endDate;

    private String orderBy;

    public static SaleTimeQuery ofTodayInstance() {
        SaleTimeQuery saleTimeQuery = new SaleTimeQuery();
        saleTimeQuery.setBeginDate(DateTimeUtils.getDateFirstTime(new Date()));
        saleTimeQuery.setEndDate(DateTimeUtils.getDateEndTime(new Date()));
        return saleTimeQuery;
    }

    public static SaleTimeQuery ofCurrrentMonthInstance() {
        SaleTimeQuery saleTimeQuery = new SaleTimeQuery();
        saleTimeQuery.setBeginDate(DateTimeUtils.getMonthFirstTime(new Date()));
        saleTimeQuery.setEndDate(DateTimeUtils.getMonthEndTime(new Date()));
        return saleTimeQuery;
    }

    public static SaleTimeQuery ofCurrentYearInstance(Date yearBeginTime) {
        // 查询对象
        SaleTimeQuery saleTimeQuery = new SaleTimeQuery();
        saleTimeQuery.setBeginDate(DateTimeUtils.getDateFirstTime(yearBeginTime));
        Date yearEndDate = DateTimeUtils.getOffsetMonth(yearBeginTime, 12);
        saleTimeQuery.setEndDate(DateTimeUtils.getDateEndTime(yearEndDate));
        return saleTimeQuery;
    }

}
