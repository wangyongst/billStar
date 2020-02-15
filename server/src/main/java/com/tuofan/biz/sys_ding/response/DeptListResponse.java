package com.tuofan.biz.sys_ding.response;

import com.tuofan.biz.sys_ding.response.base.BaseResponse;
import com.tuofan.biz.sys_orgnization.domain.entity.DingDept;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeptListResponse extends BaseResponse implements Serializable {

    private List<DingDept> department;

}
