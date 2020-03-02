package com.tuofan.setting.vo;

import com.tuofan.setting.entity.SysClass;
import com.tuofan.setting.entity.SysClassRoom;
import lombok.Data;

@Data
public class ClassRoomV extends SysClassRoom {

    private String createName;

    private String subjectName;
}
