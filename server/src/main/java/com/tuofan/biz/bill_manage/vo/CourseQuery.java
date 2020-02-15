package com.tuofan.biz.bill_manage.vo;

import com.tuofan.biz.bill_manage.entity.Course;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class CourseQuery {

    List<Integer> deptSchoolIds;

    @ApiModelProperty("教师姓名like查询")
    private String teacherNameLike;

    @ApiModelProperty("教师ID")
    private Integer teacherId;//关联教师ID

    @ApiModelProperty("学期DI")
    private Integer semesterId;

    @ApiModelProperty("字典表-班级名字（启蒙）")
    private Integer dictCourseId;

    @ApiModelProperty("班别（1,2,3,4,5,6...10）")
    private Integer courseIndex;

    @ApiModelProperty("教师IDs")
    private Collection<Integer> teacherIds;

    @ApiModelProperty("班别序号")
    private Collection<Integer> courseIndexList;

    @ApiModelProperty("科目ID集合")
    private Collection<Integer> subjectIdList;

    @ApiModelProperty("班级序号")
    private Collection<Integer> dictCourseIdList;

    private boolean isStop;

}
