package com.tuofan.dingding.vo;

import com.google.common.collect.Lists;
import com.tuofan.dingding.entity.DingDept;
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

    private List<Integer> deptSchoolIds;

    private List<DingDept> deptSchools;

    private List<DingDept> depts;


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




