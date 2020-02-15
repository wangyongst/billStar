package com.tuofan.biz.bill_manage.vo;

import lombok.Data;

@Data
public class UpdateStudentCmd {

    private Integer id;

    private String name;//姓名

    private String mobile;

    private Integer gender;//1=男，2=女

    private String schoolName;//学校；注意，该学校为学生就读学校

    private String className;//班级

    private String address;//住址

}
