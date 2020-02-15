package com.tuofan.biz.bill_manage.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
public class StudentCourseQuery {

    @ApiModelProperty("校区ID")
    private List<Integer> deptSchoolIds;

    @ApiModelProperty("班级ID")
    private List<Integer> dictCourseIdList;

    @ApiModelProperty("班别ID")
    private List<Integer> courseIndexList;

    @ApiModelProperty("学生姓名-模糊查找")
    private String studentNameLike;

    @ApiModelProperty("学生手机号-模糊查找")
    private String studentMobileLike;

    private Collection<Integer> studentIdList;

    private Collection<Integer> courseIdList;

    private Integer studentId;

    private Integer courseId;

    private Integer isExpired;

    private Integer isActive; // 状态，有效为1;

    private Date beginTime; // 开始时间

    private Date expireTime; // 过期时间

    private Date riseClassTime; // 升班时间

    private String remark; // 备注信息

    private boolean stopQuery = false;


}
