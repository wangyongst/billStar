package com.tuofan.student.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tuofan.student.entity.StudentCharge;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 学生-课程记录 服务类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-24
 */
public interface IStudentChargeService extends IService<StudentCharge> {

    IPage pageV(IPage page, QueryWrapper queryWrapper);

    IPage reportV(IPage page, QueryWrapper queryWrapper);

    IPage reportMonth(IPage page, QueryWrapper queryWrapper);

}
