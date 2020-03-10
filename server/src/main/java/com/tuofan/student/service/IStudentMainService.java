package com.tuofan.student.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tuofan.student.entity.StudentMain;

import java.util.List;

/**
 * <p>
 * 学生信息 服务类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-24
 */
public interface IStudentMainService extends IService<StudentMain> {

    IPage pageV(IPage page, QueryWrapper queryWrapper);

    IPage pageArrear(IPage page, QueryWrapper queryWrapper);

    Float sumArrear();
}
