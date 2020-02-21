package com.tuofan.basic.entity;

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
public class DingDept implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer parentId;

    private String name;

    /**
     * 是否校区：0=否；1=是；仅用于一级部门
     */
    private Boolean isSchoolZone;

    /**
     * 如是校区，则需维护校区电话
     */
    private String phone;

    /**
     * 校区群，群聊webhook地址
     */
    private String groupWebHook;

}
