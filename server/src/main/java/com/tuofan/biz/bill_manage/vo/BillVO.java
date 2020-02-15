package com.tuofan.biz.bill_manage.vo;

import com.tuofan.biz.bill_manage.entity.Student;
import com.tuofan.biz.bill_manage.service.SysDictService;
import com.tuofan.biz.sys_orgnization.domain.service.DingDeptRepository;
import com.tuofan.biz.sys_orgnization.domain.service.DingUserRepository;
import com.tuofan.core.advice.convert.Converted;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "billVO", description = "开票信息")
public class BillVO {

    @ApiModelProperty("票据ID")
    private Integer id;

    @ApiModelProperty("票据编号")
    private String billNo;

    @ApiModelProperty("校区ID")
    private Integer deptSchoolId;

    @ApiModelProperty("校区名字")
    @Converted(dependProperty = "deptSchoolId", bean = DingDeptRepository.class)
    private String deptSchoolName;

    @ApiModelProperty("校区电话")
    @Converted(dependProperty = "deptSchoolId", refLabel = "phone", bean = DingDeptRepository.class)
    private String deptSchoolPhone;

    @ApiModelProperty("学生ID")
    private Integer studentId;

    @ApiModelProperty("学生数据")
    private Student student;

    @ApiModelProperty("金额")
    private Double amount;

    @ApiModelProperty("票据类型")
    private Integer type;

    @ApiModelProperty("支付类型ID")
    private Integer payTypeId;

    @ApiModelProperty("支付类型名称")
    @Converted(dependProperty = "payTypeId", bean = SysDictService.class)
    private String payTypeName;

    @ApiModelProperty("开票时间")
    private Date billTime;

    @ApiModelProperty("开票人ID")
    private Integer billCreator;

    @ApiModelProperty("开票人名字")
    @Converted(dependProperty = "billCreator", bean = DingUserRepository.class)
    private String billCreatorName;

    @ApiModelProperty("备注信息")
    private String remark;

    @ApiModelProperty("是否转校")
    private Integer isTransferred;

    @ApiModelProperty("票据关联的课程数据")
    private List<BillCourseVO> courses;

    @ApiModelProperty("初始欠费。开票等操作的时候的欠费。")
    private Double initialArrears; // 初始欠费。

    @ApiModelProperty("当前欠费。每次补费后，会被更新。")
    private Double currentArrears;

}
