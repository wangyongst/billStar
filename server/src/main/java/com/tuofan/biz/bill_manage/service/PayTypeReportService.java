package com.tuofan.biz.bill_manage.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuofan.biz.bill_manage.entity.SysDict;
import com.tuofan.biz.bill_manage.vo.DynamicTableColumn;
import com.tuofan.biz.bill_manage.vo.PayTypeSaleDTO;
import com.tuofan.biz.bill_manage.vo.PayTypeSaleVO;
import com.tuofan.biz.bill_manage.vo.SaleQuery;
import com.tuofan.biz.sys_orgnization.domain.entity.DingDept;
import com.tuofan.biz.sys_orgnization.domain.service.DingDeptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PayTypeReportService {

    @Autowired
    BillQueryService billQueryService;

    @Autowired
    DingDeptRepository dingDeptRepository;

    @Autowired
    SysDictService sysDictService;

    /**
     * 整理成key = payTypeId , value = amount 的map list
     * list 每一行代表一个校区，校区每一列代表一个统计值
     *
     * @param query
     * @return
     */
    public List<PayTypeSaleVO> payTypeReport(SaleQuery query) {
        List<PayTypeSaleDTO> list = billQueryService.payTypeReport(query);
        Map<String, PayTypeSaleDTO> saleMap = this.mapSales(list);
        List<SysDict> payTypes = sysDictService.listPayType();
        List<DingDept> schoolDepartments = dingDeptRepository.listByIds(query.getDeptSchoolIds());
        List<PayTypeSaleVO> result = Lists.newArrayList();
        for (DingDept dept : schoolDepartments) {
            PayTypeSaleVO payTypeSaleVO = new PayTypeSaleVO();
            payTypeSaleVO.setDeptSchoolId(dept.getId());
            payTypeSaleVO.setDeptSchoolName(dept.getName());
            Map<String, Double> payTypeSaleMap = Maps.newHashMap();
            for (SysDict payType : payTypes) {
                // 该校区，该支付类型多少钱
                String key = generateKey(dept.getId(), payType.getId());
                Double amount = saleMap.keySet().contains(key) ? saleMap.get(key).getAmount() : 0.0;
                payTypeSaleMap.put(generateFieldName(payType.getId()), amount);
            }
            payTypeSaleVO.setPayTypeSaleMap(payTypeSaleMap);
            result.add(payTypeSaleVO);
        }
        this.calculateAndAppendTotal(result);
        return result;
    }

    /**
     * 以校区—支付类型为key，组map
     *
     * @param list
     * @return
     */
    private Map<String, PayTypeSaleDTO> mapSales(List<PayTypeSaleDTO> list) {
        Map<String, PayTypeSaleDTO> map = Maps.newHashMap();
        for (PayTypeSaleDTO ele : list) {
            map.put(generateKey(ele.getDeptSchoolId(), ele.getPayTypeId()), ele);
        }
        return map;
    }

    private String generateKey(Integer deptId, Integer payTypeId) {
        return String.format("%s-%s", deptId, payTypeId);
    }

    public List<DynamicTableColumn> listDynamicColumn() {
        List<SysDict> payTypes = sysDictService.listPayType();
        List<DynamicTableColumn> list = Lists.newArrayList();
        for (SysDict payType : payTypes) {
            DynamicTableColumn column = DynamicTableColumn.of(list.size(), payType.getName(), generateFieldName(payType.getId()));
            list.add(column);
        }
        return list;
    }


    /**
     * 行与列的总和计算
     *
     * @param list
     */
    private void calculateAndAppendTotal(List<PayTypeSaleVO> list) {
        PayTypeSaleVO total = new PayTypeSaleVO();
        total.setIsTotal(true);
        total.setDeptSchoolName("总计");
        total.setPayTypeSaleMap(Maps.newHashMap());
        for (PayTypeSaleVO ele : list) {
            Double thisDeptTotal = 0.0;
            for (String key : ele.getPayTypeSaleMap().keySet()) {
                Double curDeptPayTypeAmount = ele.getPayTypeSaleMap().get(key);
                if (!total.getPayTypeSaleMap().containsKey(key)) {
                    total.getPayTypeSaleMap().put(key, 0.0);
                }
                Double curPayTypeTotal = total.getPayTypeSaleMap().get(key) + curDeptPayTypeAmount;
                total.getPayTypeSaleMap().put(key, curPayTypeTotal);
                thisDeptTotal += curDeptPayTypeAmount;
            }
            ele.setDeptTotal(thisDeptTotal);
        }
        Double amountTotal = 0.0;
        for (String key : total.getPayTypeSaleMap().keySet()) {
            amountTotal += total.getPayTypeSaleMap().get(key);
        }
        total.setDeptTotal(amountTotal);
        list.add(total);
    }

//    private void setMapToField(List<PayTypeSaleVO> list) {
//        BeanGenerator generator = new BeanGenerator();
//        for (PayTypeSaleVO ele : list) {
//            for (Integer key : ele.getPayTypeSaleMap().keySet()) {
//                ele.put(generateFieldName(key), ele.getPayTypeSaleMap().get(key));
//            }
//        }
//    }

    private String generateFieldName(Integer key) {
        return "field_" + Integer.toString(key);
    }

}
