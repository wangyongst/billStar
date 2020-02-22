package com.tuofan.setting.service;

import com.tuofan.setting.entity.SysSemester;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tuofan.setting.vo.SemesterV;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-22
 */
public interface ISysSemesterService extends IService<SysSemester> {

     List<SemesterV> listV();

     void allNotDefault();
}
