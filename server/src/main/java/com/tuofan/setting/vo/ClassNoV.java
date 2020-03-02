package com.tuofan.setting.vo;

import com.tuofan.setting.entity.SysClass;
import com.tuofan.setting.entity.SysClassNo;
import lombok.Data;

@Data
public class ClassNoV extends SysClassNo {

    private String createName;

    private String subjectName;
}
