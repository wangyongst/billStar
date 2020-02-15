package com.tuofan.biz.bill_manage.vo;

import lombok.Data;

@Data
public class YearSaleVO {

    private Integer deptSchoolId;

    private String deptSchoolName;

    private Double sale1;

    private Double sale2;

    private Double sale3;

    private Double sale4;

    private Double sale5;

    private Double sale6;

    private Double sale7;

    private Double sale8;

    private Double sale9;

    private Double sale10;

    private Double sale11;

    private Double sale12;

    private Double sale13;

    public static YearSaleVO of() {
        YearSaleVO item = new YearSaleVO();
        item.setSale1(0.0);
        item.setSale2(0.0);
        item.setSale3(0.0);
        item.setSale4(0.0);
        item.setSale5(0.0);
        item.setSale6(0.0);
        item.setSale7(0.0);
        item.setSale8(0.0);
        item.setSale9(0.0);
        item.setSale10(0.0);
        item.setSale11(0.0);
        item.setSale12(0.0);
        item.setSale13(0.0);
        return item;
    }


}
