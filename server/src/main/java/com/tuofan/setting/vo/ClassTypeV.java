package com.tuofan.setting.vo;

import com.tuofan.setting.entity.SysClassType;
import lombok.Data;

@Data
public class ClassTypeV extends SysClassType {

    private String createName;

    private String subjectName;
}
