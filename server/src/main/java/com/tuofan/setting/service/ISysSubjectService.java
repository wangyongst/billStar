package com.tuofan.setting.service;

import com.tuofan.setting.entity.SysSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tuofan.setting.vo.SubjectV;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-22
 */
public interface ISysSubjectService extends IService<SysSubject> {

    List<SubjectV> listV();
}
