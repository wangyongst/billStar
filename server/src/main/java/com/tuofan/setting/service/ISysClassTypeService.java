package com.tuofan.setting.service;

import com.tuofan.setting.entity.SysClassType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tuofan.setting.vo.ClassTypeV;
import com.tuofan.setting.vo.SemesterV;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-23
 */
public interface ISysClassTypeService extends IService<SysClassType> {

    List<ClassTypeV> listV();

}
