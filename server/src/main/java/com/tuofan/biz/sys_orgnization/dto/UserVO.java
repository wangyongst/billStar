package com.tuofan.biz.sys_orgnization.dto;

import com.google.common.collect.Lists;
import com.tuofan.biz.sys_orgnization.domain.entity.DingDept;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

@Data
public class UserVO {

    private Integer id;

    private String userid;

    private String name;

    private String mobile;

    private String position;

    private String email;

    private String avatar;

    private Boolean isAdmin;

    private Boolean isLeader;

    private Integer isTeacher;

    private Integer isAppAdmin;

    @ApiModelProperty("校区ID")
    private List<Integer> deptSchoolIds;

    @ApiModelProperty("所有校区")
    private List<DingDept> deptSchools;

    @ApiModelProperty("所有部门")
    private List<DingDept> depts;

//    private Set<DeptVO> departments = Sets.newHashSet();


    public void initCollectionElements() {
        if (CollectionUtils.isEmpty(this.getDeptSchoolIds())) {
            this.setDeptSchoolIds(Lists.newArrayList());
        }
        if (CollectionUtils.isEmpty(this.getDeptSchools())) {
            this.setDeptSchools(Lists.newArrayList());
        }
        if (CollectionUtils.isEmpty(this.getDepts())) {
            this.setDepts(Lists.newArrayList());
        }
    }
}




