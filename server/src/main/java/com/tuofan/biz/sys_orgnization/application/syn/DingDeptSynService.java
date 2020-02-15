package com.tuofan.biz.sys_orgnization.application.syn;

import com.google.common.collect.Lists;
import com.tuofan.biz.sys_configs.service.ConfigCachedUtils;
import com.tuofan.biz.sys_ding.request.DeptRequest;
import com.tuofan.biz.sys_orgnization.domain.entity.DingDept;
import com.tuofan.biz.sys_orgnization.domain.service.DingDeptRepository;
import com.tuofan.biz.sys_orgnization.dto.SynResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DingDeptSynService {

    @Autowired
    DeptRequest deptRequest;

    @Autowired
    DingDeptRepository dingDeptRepository;

    @Autowired
    ConfigCachedUtils configCachedUtils;

    @Transactional(rollbackFor = Exception.class)
    public SynResultVO syn() {
        SynResultVO result = new SynResultVO();
        List<DingDept> list = deptRequest.listAll();
        if (CollectionUtils.isEmpty(list)) {
            result.setMsg("没有数据需要同步处理");
            return result;
        }
        list.forEach(DingDept::initNotDingFields);
//        Map<Integer, DingDept> entityMap = list.stream().collect(Collectors.toMap(DingDept::getId, e -> e));
//        list.forEach(ele -> ele.findParentSchoolDept(entityMap, configCachedUtils.getDingRootDeptId()));
        List<DingDept> existed = dingDeptRepository.listAll(null);
        if (CollectionUtils.isEmpty(existed)) {
            result.setInsertCnt(dingDeptRepository.createList(list));
            return result;
        }
        Map<Integer, DingDept>
                existMap = existed.stream().collect(Collectors.toMap(DingDept::getId, x -> x));
        List<DingDept> insertList = Lists.newArrayList();
        List<DingDept> updateList = Lists.newArrayList();
        for (DingDept ele : list) {
            if (!existMap.containsKey(ele.getId())) {
                insertList.add(ele);
                continue;
            }
            DingDept existEle = existMap.get(ele.getId());
            if (ele.needUpdateCompareTo(existEle)) {
                updateList.add(ele);
            }
        }
        if (!CollectionUtils.isEmpty(insertList)) {
            result.setInsertCnt(dingDeptRepository.createList(insertList));
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            result.setUpdateCnt(dingDeptRepository.updateList(updateList));
        }
        return result;
    }
}
