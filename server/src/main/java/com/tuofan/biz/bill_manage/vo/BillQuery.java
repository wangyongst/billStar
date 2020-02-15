package com.tuofan.biz.bill_manage.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class BillQuery {

    private Date startDate;

    private Date endDate;

    private Integer semesterId;

    private List<Integer> deptSchoolIds;

    private Date expireEndTime;

    private Collection<Integer> ids;

    private boolean stop = false;

    private Integer type;

    private Integer payTypeId;

    private Integer hasArrears;

    @ApiModelProperty("科目-ID-list")
    private Collection<Integer> subjectIdList;

    @ApiModelProperty("班别-list")
    private Collection<Integer> courseIndexList;

    @ApiModelProperty("班级-list")
    private Collection<Integer> dictCourseIdList;

    @ApiModelProperty("票据类型-list")
    private Collection<Integer> typeList;

    private Collection<Integer> studentIdList;

    private Integer studentId;

    private String studentName;

}
