package com.tuofan.report.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

@Data
public class ReportV {

    public List header;

    public List pageRecords;
}
