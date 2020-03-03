package com.tuofan.setting.service;

import com.tuofan.setting.entity.SysClassNo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tuofan.setting.vo.ClassV;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangyong
 * @since 2020-03-01
 */
public interface ISysClassNoService extends IService<SysClassNo> {

    List listV();

}
