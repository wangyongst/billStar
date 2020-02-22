package com.tuofan.orgination.application;

import com.google.common.collect.Lists;
import com.tuofan.configs.service.ConfigCachedUtils;
import com.tuofan.core.Result;
import com.tuofan.ding.request.DeptRequest;
import com.tuofan.orgination.entity.DingDept;
import com.tuofan.orgination.service.IDingDeptService;
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
    IDingDeptService iDingDeptService;

    @Autowired
    ConfigCachedUtils configCachedUtils;

    @Transactional(rollbackFor = Exception.class)
    public Result syn() {
        List<DingDept> list = deptRequest.listAll();
        if (CollectionUtils.isEmpty(list)) {
            return Result.error("没有数据需要同步处理");
        }
        List<DingDept> existed = iDingDeptService.list();
        if (CollectionUtils.isEmpty(existed)) {
            iDingDeptService.saveBatch(list);
            return Result.ok();
        }
        Map<Integer, DingDept> existMap = existed.stream().collect(Collectors.toMap(DingDept::getId, x -> x));
        List<DingDept> insertList = Lists.newArrayList();
        List<DingDept> updateList = Lists.newArrayList();
        for (DingDept ele : list) {
            if (!existMap.containsKey(ele.getId())) {
                insertList.add(ele);
                continue;
            }
            DingDept existEle = existMap.get(ele.getId());
            updateList.add(makeDept(ele,existEle));
        }
        if (!CollectionUtils.isEmpty(insertList)) {
            iDingDeptService.saveBatch(insertList);
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            iDingDeptService.saveOrUpdateBatch(updateList);
        }
        return Result.ok();
    }

    public DingDept makeDept(DingDept o, DingDept t) {
        o.setGroupWebHook(t.getGroupWebHook());
        o.setIsSchoolZone(t.getIsSchoolZone());
        o.setPhone(t.getPhone());
        return o;
    }
}
