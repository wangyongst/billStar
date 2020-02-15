package com.tuofan.biz.bill_manage.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuofan.biz.bill_manage.constants.DictTypes;
import com.tuofan.biz.bill_manage.dao.StudentCourseDao;
import com.tuofan.biz.bill_manage.entity.Course;
import com.tuofan.biz.bill_manage.entity.SysDict;
import com.tuofan.biz.bill_manage.vo.*;
import com.tuofan.biz.sys_orgnization.domain.service.DingDeptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubjectStudentReportService {

    @Autowired
    BillQueryService billQueryService;

    @Autowired
    DingDeptRepository dingDeptRepository;

    @Autowired
    SysDictService sysDictService;

    @Autowired
    CourseService courseService;

    @Autowired
    StudentCourseService studentCourseService;

    /**
     * 报表数据
     *
     * @param query 查询对象
     * @return 报表的数据
     */
    public List<SubjectStudentCountVO> subjectStudentReport(SubjectStudentQuery query) {
        List<Course> courses = courseService.listBySemesterId(query.getSemesterId());
        if (CollectionUtils.isEmpty(courses)) {
            return Lists.newArrayList();
        }
        List<Integer> courseIds = courses.stream().map(Course::getId).collect(Collectors.toList());
        query.setCourseIds(courseIds);
        // 查询数据库数据
        List<SubjectStudentDTO> list = studentCourseService.subjectStudentReport(query);
        // 做组装
        List<SubjectStudentCountVO> cntList = this.processSubjectStudentCountVO(list);
        SubjectStudentCountVO total = this.processSubjectTotalCountVO(cntList);
        cntList.add(total);
        return cntList;
    }

    /**
     * 数据汇总
     *
     * @param cntList 列表
     * @return 科目汇总结果
     */
    private SubjectStudentCountVO processSubjectTotalCountVO(List<SubjectStudentCountVO> cntList) {
        SubjectStudentCountVO subjectTotal = new SubjectStudentCountVO();
        subjectTotal.setIsTotal(true);
        subjectTotal.setDeptSchoolName("总计");
        subjectTotal.setSubjectStudentMap(Maps.newHashMap());
        for (SubjectStudentCountVO model : cntList) {
            Map<String, SubjectCountVO> map = model.getSubjectStudentMap();
            Map<String, SubjectCountVO> totalMap = subjectTotal.getSubjectStudentMap();
            for (String subjectId : map.keySet()) {
                SubjectCountVO totalVO = totalMap.get(subjectId);
                SubjectCountVO curVO = map.get(subjectId);
                if (totalVO == null) {
                    totalVO = SubjectCountVO.of(curVO.getInDateCnt(), curVO.getOutDateCnt());
                } else {
                    totalVO.setInDateCnt(totalVO.getInDateCnt() + curVO.getInDateCnt());
                    totalVO.setOutDateCnt(totalVO.getOutDateCnt() + curVO.getOutDateCnt());
                }
                totalMap.put(subjectId, totalVO);
            }
            // 处理部门-多科目汇总
        }
        // 处理总总计
        SubjectCountVO totalTotalVO = new SubjectCountVO();
        totalTotalVO.setInDateCnt(0);
        totalTotalVO.setOutDateCnt(0);
        for (String subjectId : subjectTotal.getSubjectStudentMap().keySet()) {
            SubjectCountVO countVO = subjectTotal.getSubjectStudentMap().get(subjectId);
            totalTotalVO.setInDateCnt(totalTotalVO.getInDateCnt() + countVO.getInDateCnt());
            totalTotalVO.setOutDateCnt(totalTotalVO.getOutDateCnt() + countVO.getOutDateCnt());
        }
        subjectTotal.setDeptTotal(totalTotalVO);
        return subjectTotal;
    }

    public void processDeptSubjectTotal(SubjectStudentCountVO model, SubjectStudentDTO dto) {
        SubjectCountVO deptTotal = model.getDeptTotal();
        if (deptTotal == null) {
            deptTotal = SubjectCountVO.of(dto.getInDateCnt(), dto.getOutDateCnt());
        } else {
            deptTotal.setInDateCnt(deptTotal.getInDateCnt() + dto.getInDateCnt());
            deptTotal.setOutDateCnt(deptTotal.getOutDateCnt() + dto.getOutDateCnt());
        }
        model.setDeptTotal(deptTotal);
    }

    /**
     * 处理数据列表
     *
     * @param list 列表
     * @return 数据列表
     */
    private List<SubjectStudentCountVO> processSubjectStudentCountVO(List<SubjectStudentDTO> list) {
        // 一个dept一行数据
        Map<Integer, SubjectStudentCountVO> map = Maps.newHashMap();
        for (SubjectStudentDTO dto : list) {
            if (!map.containsKey(dto.getDeptSchoolId())) {
                SubjectStudentCountVO item = new SubjectStudentCountVO();
                item.setDeptSchoolId(dto.getDeptSchoolId());
                item.setDeptSchoolName(dto.getDeptSchoolName());
                map.put(dto.getDeptSchoolId(), item);
            }
            SubjectStudentCountVO item = map.get(dto.getDeptSchoolId());
            // 将数据塞到对应的科目
            if (item.getSubjectStudentMap() == null) {
                item.setSubjectStudentMap(Maps.newHashMap());
            }
            SubjectCountVO cntVO = SubjectCountVO.of(dto.getInDateCnt(), dto.getOutDateCnt());
            item.getSubjectStudentMap().put(dto.getSubjectId().toString(), cntVO);
            // 处理部门-多科目汇总
            this.processDeptSubjectTotal(item, dto);
        }
        return new ArrayList<>(map.values());

    }

    /**
     * 处理标题
     *
     * @return 处理动态标题
     */
    public List<DynamicTableColumn> listDynamicColumn() {
        List<SysDict> subjects = sysDictService.listByType(DictTypes.SUBJECT);
        int index = 0;
        List<DynamicTableColumn> result = Lists.newArrayList();
        for (SysDict ele : subjects) {
            String dynamicTitle = ele.getName() + "(在校/超期)";
            DynamicTableColumn dynamicTableColumn = DynamicTableColumn.of(index, dynamicTitle, ele.getId().toString());
            index++;
            result.add(dynamicTableColumn);
        }
        return result;
    }

}
