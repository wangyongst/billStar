package com.tuofan.ding.response;


import com.tuofan.dingding.entity.DingDept;
import com.tuofan.ding.response.base.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeptListResponse extends BaseResponse implements Serializable {

    private List<DingDept> department;

}
