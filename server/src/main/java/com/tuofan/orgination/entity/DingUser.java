package com.tuofan.orgination.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangyong
 * @since 2020-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DingUser implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String userId;

    private String name;

    private String email;

    private String mobile;

    private String position;

    private String avatar;

    /**
     * 是否企业管理员，超管
     */
    private Boolean isAdmin;

    /**
     * 是否教师
     */
    private Boolean isTeacher;

    /**
     * 是否部门领导
     */
    private Boolean isLeader;

    /**
     * 系统管理员
     */
    private Integer isAppAdmin;


}
