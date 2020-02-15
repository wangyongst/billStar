package com.tuofan.biz.sys_orgnization.domain.entity;

import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.persistence.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "ding_user")
public class DingUser extends BaseEntity implements Serializable {

    @Id
    private Integer id;

    @Column(name = "user_id")
    private String userid;

    private String name;

//    @ApiModelProperty("校区ID")
//    private Integer deptSchoolId;

    private String mobile;

    private String position;

    private String email;

    private String avatar;

    private Boolean isAdmin;

    private Boolean isLeader;

    private Integer isTeacher;

    private List<Integer> department;

    private Integer isAppAdmin;

    public String toString() {
        StringBuffer builder = new StringBuffer();
//        if (this.getDeptSchoolId() != null) {
//            builder.append(this.getDeptSchoolId().toString());
//        }
        builder.append(this.getUserid())
                .append(this.getName())
                .append(getMobile())
                .append(getPosition())
                .append(getEmail())
                .append(getIsAdmin())
                .append(getIsLeader());
        return builder.toString();
    }

    /**
     * 设置默认值，钉钉同步不报错
     */
    public void initFieldsNotInDingDing() {
        this.setIsTeacher(AppConstants.intNo);
        this.setIsAppAdmin(AppConstants.intNo);
    }

}
