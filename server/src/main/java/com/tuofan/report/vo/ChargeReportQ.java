package com.tuofan.report.vo;

import com.tuofan.core.SearchQ;
import lombok.Data;

import java.util.Date;

@Data
public class ChargeReportQ extends SearchQ {

    private Date begin;

    private Date end;
}
