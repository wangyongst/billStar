package com.tuofan.biz.bill_manage.web;

import com.github.pagehelper.PageInfo;
import com.tuofan.biz.bill_manage.component.BillNoticeService;
import com.tuofan.biz.bill_manage.entity.StudentCourse;
import com.tuofan.biz.bill_manage.service.StudentCourseService;
import com.tuofan.biz.bill_manage.vo.StudentCourseQuery;
import com.tuofan.biz.bill_manage.vo.StudentCourseVO;
import com.tuofan.core.advice.auth.LoginRequired;
import com.tuofan.core.advice.convert.FieldConversion;
import com.tuofan.core.dto.BaseResult;
import com.tuofan.core.dto.PageRequest;
import com.tuofan.core.helper.ModelConvertHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller
@RequestMapping("${cert.api.prefix}/billNotice")
@Slf4j
public class BillNoticeController {

    @Autowired
    private BillNoticeService billNoticeService;

    /**
     * 分页查询
     *
     * @return baseResult
     */
    @GetMapping("triggerNotice")
    public BaseResult sendSaleNotice() {
        log.info("sending sale notice");
        return billNoticeService.triggerNotice();
    }


}
