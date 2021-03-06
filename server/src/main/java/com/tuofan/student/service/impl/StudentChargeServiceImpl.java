package com.tuofan.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tuofan.report.vo.ChargeReportV;
import com.tuofan.student.entity.StudentCharge;
import com.tuofan.student.mapper.StudentChargeMapper;
import com.tuofan.student.service.IStudentChargeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuofan.student.vo.BillV;
import com.tuofan.student.vo.StudentChargeV;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 学生-课程记录 服务实现类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-24
 */
@Service
public class StudentChargeServiceImpl extends ServiceImpl<StudentChargeMapper, StudentCharge> implements IStudentChargeService {

    @Override
    public IPage pageV(IPage page, QueryWrapper queryWrapper) {
        return baseMapper.pageV(page, queryWrapper);
    }

    @Override
    public BillV getByChargeId(Integer id) {
        return baseMapper.getByChargeId(id);
    }

    @Override
    public List reportMonth(QueryWrapper queryWrapper) {
        return baseMapper.reportMonth(queryWrapper);
    }

    @Override
    public List reportMonthTotal(QueryWrapper queryWrapper) {
        return baseMapper.reportMonthTotal(queryWrapper);
    }

    @Override
    public List<ChargeReportV> reportChargeType(QueryWrapper queryWrapper) {
        return baseMapper.reportChargeType(queryWrapper);
    }

    @Override
    public List<ChargeReportV> reportChargeTypeTotal(QueryWrapper queryWrapper) {
        return baseMapper.reportChargeTypeTotal(queryWrapper);
    }

    @Override
    public List<StudentChargeV> listStudentChargeV(QueryWrapper queryWrapper) {
        return baseMapper.listStudentChargeV(queryWrapper);
    }
}
