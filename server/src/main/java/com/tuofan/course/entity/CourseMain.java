package com.tuofan.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 课程信息
 * </p>
 *
 * @author wangyong
 * @since 2020-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CourseMain implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学期
     */
    private Integer semesterId;

    /**
     * 关联教师ID
     */
    private Integer teacherId;

    private Integer classId;

    private Integer schoolId;

    private Integer classNoId;

    private String classNo;

    private Integer classNum;

    private Integer classRoomId;

    private String classRoom;

    private String timeIds;

    private String className;

    private String teacherName;

    private String courseTime;

    private Integer studentNum;

}
