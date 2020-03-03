package com.tuofan.setting.service;

import com.tuofan.setting.entity.SysClassRoom;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangyong
 * @since 2020-03-01
 */
public interface ISysClassRoomService extends IService<SysClassRoom> {

    List listV();

}
