package com.tuofan.student.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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

    private static final long serialVersionUID=1L;

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

    /**
     * 学校；注意，该学校为学生就读学校
     */
    private String myschool;

    /**
     * 班级
     */
    private String myclass;

    /**
     * 住址
     */
    private String address;

    private Boolean accept;

    private String remark;


}
