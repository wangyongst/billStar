package com.tuofan.setting.service;

import com.tuofan.setting.entity.SysClass;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tuofan.setting.vo.ClassV;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-23
 */
public interface ISysClassService extends IService<SysClass> {

    List<ClassV> listV();

}
