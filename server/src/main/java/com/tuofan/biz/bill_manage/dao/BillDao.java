package com.tuofan.biz.bill_manage.dao;

import com.tuofan.biz.bill_manage.entity.Bill;
import com.tuofan.biz.bill_manage.vo.PayTypeSaleDTO;
import com.tuofan.biz.bill_manage.vo.SaleDTO;
import com.tuofan.biz.bill_manage.vo.SaleTimeQuery;
import com.tuofan.biz.bill_manage.vo.YearSaleDto;
import com.tuofan.core.persistence.dao.BaseDao;

import java.util.Date;
import java.util.List;

public interface BillDao extends BaseDao<Bill> {

    List<YearSaleDto> yearReport(Date beginDate, Date endDate, String deptSchoolIdsStr);

    List<PayTypeSaleDTO> payTypeReport(Date beginDate, Date endDate, String deptSchoolIdsStr);

    List<SaleDTO> saleReport(SaleTimeQuery query);

}