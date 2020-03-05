package com.tuofan.report.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

@Data
public class YearReportV {

    public List header;

    public IPage pageRecords;
}
