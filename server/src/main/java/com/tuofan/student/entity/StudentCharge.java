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
 * 学生-课程记录
 * </p>
 *
 * @author wangyong
 * @since 2020-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StudentCharge implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学生ID
     */
    private Integer studentId;

    private Integer chargeId;

    private Float amount;

    //1 新生 2 续费 3补费
    private Integer type;

    private String createBy;

    private Date createTime;

}
