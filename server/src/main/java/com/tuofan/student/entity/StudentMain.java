package com.tuofan.student.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学生信息
 * </p>
 *
 * @author wangyong
 * @since 2020-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StudentMain implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 1=男，0=女
     */
    private Boolean sex;

    private Integer myschoolId;

    private String myschool;

    private Integer myclassId;

    private String myclass;
    /**
     * 住址
     */
    private String address;

    private Boolean accept;

    private String remark;

    //0 正常 1欠费 2流失 3休学
    private Integer type;

    private Integer schoolId;

    private Date lostTime;

    private Float arrears;

    private String createBy;

    private Date createTime;

    private Date fuTime;
}
