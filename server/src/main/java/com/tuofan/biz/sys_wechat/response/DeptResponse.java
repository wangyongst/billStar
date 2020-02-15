package com.tuofan.biz.sys_wechat.response;

import com.tuofan.biz.sys_wechat.response.dto.RDepartment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeptResponse extends BaseResponse {

    public List<RDepartment> department;

}