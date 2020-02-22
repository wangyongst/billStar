package com.tuofan.orgination.vo;

import com.tuofan.orgination.entity.DingDept;
import com.tuofan.orgination.entity.DingUser;
import lombok.Data;

import java.util.List;

@Data
public class TeacherV extends DingUser {

    private List<DingDept> schoolList;
}
