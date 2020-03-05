package com.tuofan.setting.service;

import com.tuofan.setting.entity.SysMySchool;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyong
 * @since 2020-03-04
 */
public interface ISysMySchoolService extends IService<SysMySchool> {

    List listV();

}
