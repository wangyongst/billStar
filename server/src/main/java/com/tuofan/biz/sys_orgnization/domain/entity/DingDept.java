package com.tuofan.biz.sys_orgnization.domain.entity;

import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.persistence.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "ding_dept")
public class DingDept extends BaseEntity {

    private Integer id;

    @Column(name = "parent_id")
    private Integer parentid;

    @ApiModelProperty("是否校区")
    private Integer isSchoolZone;

    @ApiModelProperty("联系电话")
    private String phone;

    private String name;

    private String groupWebHook;

    public boolean needUpdateCompareTo(DingDept existEle) {
        return (this.getParentid() != null && !this.getParentid().equals(existEle.getParentid())
                || !this.getName().equals(existEle.getName()));
    }

    public void initNotDingFields() {
        this.setIsSchoolZone(AppConstants.intNo);
    }

//    @Transient
//    private Integer parentSchoolDeptId;

//    public void findParentSchoolDept(Map<Integer, DingDept> map, Integer rootCorpDeptId) {
//        Integer id = this.getId();
//        Integer parentId = this.getParentid();
//        Integer parentSchoolId = this.getParentSchoolDeptId();
//        if (id.equals(rootCorpDeptId) || (parentSchoolId != null && parentSchoolId > 0)) {
//            return;
//        }
//        if (parentId.equals(rootCorpDeptId)) {
//            this.setParentSchoolDeptId(getId());
//            return;
//        }
//        if (map.keySet().contains(parentId)) {
//            DingDept parent = map.get(parentId);
//            if (parent.getParentSchoolDeptId() == null) {
//                parent.findParentSchoolDept(map, rootCorpDeptId);
//            }
//            this.setParentSchoolDeptId(parent.getParentSchoolDeptId());
//        }
//
//    }

}
