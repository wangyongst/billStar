package com.tuofan.biz;

import com.tuofan.biz.bill_manage.component.BillOperateMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@RequestMapping("${cert.api.prefix}/test")
public class TestController {

    @Autowired
    BillOperateMessageService billOperateMessageService;

    @GetMapping("billMessageSend")
    public void billMessageSend(@RequestParam Integer billId) {
        billOperateMessageService.processBillMessage(billId);
    }

}
