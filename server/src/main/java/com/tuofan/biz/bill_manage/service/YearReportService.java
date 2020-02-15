package com.tuofan.biz.bill_manage.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuofan.biz.bill_manage.vo.SaleOfYearQuery;
import com.tuofan.biz.bill_manage.vo.SaleQuery;
import com.tuofan.biz.bill_manage.vo.YearSaleDto;
import com.tuofan.biz.bill_manage.vo.YearSaleVO;
import com.tuofan.biz.sys_orgnization.domain.entity.DingDept;
import com.tuofan.biz.sys_orgnization.domain.service.DingDeptRepository;
import com.tuofan.core.utils.DateTimeUtils;
import com.tuofan.core.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class YearReportService {

    @Autowired
    BillQueryService billQueryService;

    @Autowired
    DingDeptRepository dingDeptRepository;

    public List<YearSaleVO> yearReport(SaleQuery query) {
        List<YearSaleDto> list = billQueryService.yearReport(query);
        Map<String, YearSaleDto> mapSales = this.mapSales(list);
        List<DingDept> depts = dingDeptRepository.listByIds(query.getDeptSchoolIds());
        List<Integer> keys = get12Keys(query.getBeginDate());
        List<YearSaleVO> resultList = Lists.newArrayList();
        depts.forEach(dept -> {
            YearSaleVO result = YearSaleVO.of();
            result.setDeptSchoolId(dept.getId());
            result.setDeptSchoolName(dept.getName());
            for (int i = 0; i < 12; i++) {
                try {
                    String key = dept.getId() + "-" + keys.get(i);
                    if (mapSales.containsKey(key)) {
                        YearSaleDto dto = mapSales.get(key);
                        String fieldName = "sale" + Integer.toString(i + 1);
                        ReflectionUtils.setFieldValue(result, fieldName, dto.getAmount());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            resultList.add(result);
        });
        this.calculateTotal(resultList);
        return resultList;
    }

    private void calculateTotal(List<YearSaleVO> list) {
        list.forEach(ele -> {
            Double total = ele.getSale1() + ele.getSale2() + ele.getSale3()
                    + ele.getSale4() + ele.getSale5() + ele.getSale6()
                    + ele.getSale7() + ele.getSale8() + ele.getSale9()
                    + ele.getSale10() + ele.getSale11() + ele.getSale12();
            ele.setSale13(total);
        });
        YearSaleVO total = YearSaleVO.of();
        for (YearSaleVO ele : list) {
            total.setDeptSchoolName("总计");
            total.setSale1(total.getSale1() + ele.getSale1());
            total.setSale2(total.getSale2() + ele.getSale2());
            total.setSale3(total.getSale3() + ele.getSale3());
            total.setSale4(total.getSale4() + ele.getSale4());
            total.setSale5(total.getSale5() + ele.getSale5());
            total.setSale6(total.getSale6() + ele.getSale6());
            total.setSale7(total.getSale7() + ele.getSale7());
            total.setSale8(total.getSale8() + ele.getSale8());
            total.setSale9(total.getSale9() + ele.getSale9());
            total.setSale10(total.getSale10() + ele.getSale10());
            total.setSale11(total.getSale11() + ele.getSale11());
            total.setSale12(total.getSale12() + ele.getSale12());
            total.setSale13(total.getSale13() + ele.getSale13());
        }
        list.add(total);
    }

    private Map<String, YearSaleDto> mapSales(List<YearSaleDto> list) {
        Map<String, YearSaleDto> map = Maps.newHashMap();
        for (YearSaleDto ele : list) {
            map.put(ele.getDeptSchoolId() + "-" + ele.getYearMonth(), ele);
        }
        return map;
    }

    private Integer getYearMonthInteger(Date date) {
        String str = DateTimeUtils.formatDateTime(date, DateTimeUtils.DATE_FORMAT_NOJOINER_MONTH);
        return Integer.valueOf(str);
    }

    private List<Integer> get12Keys(Date date) {
        List<Integer> list = Lists.newArrayList();
        for (int i = 0; i < 12; i++) {
            Date tmp = DateTimeUtils.getOffsetMonth(date, i);
            list.add(getYearMonthInteger(tmp));
        }
        return list;
    }


    public List<Date> yearReportTitle(SaleOfYearQuery query) {
        List<Date> list = Lists.newArrayList();
        Date date = DateTimeUtils.getMonthFirstTime(query.getStartMonth());
        for (int i = 0; i < 12; i++) {
            list.add(DateTimeUtils.getOffsetMonth(date, i));
        }
        return list;
    }
}
