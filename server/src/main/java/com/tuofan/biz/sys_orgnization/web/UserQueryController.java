package com.tuofan.biz.sys_orgnization.web;

import com.github.pagehelper.PageInfo;
import com.tuofan.biz.sys_orgnization.application.query.DingUserQueryService;
import com.tuofan.biz.sys_orgnization.dto.UserVO;
import com.tuofan.biz.sys_orgnization.dto.query.ExtUserQuery;
import com.tuofan.core.dto.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${cert.api.prefix}/userQuery")
public class UserQueryController {


    @Autowired
    public DingUserQueryService dingUserQueryService;

    @PostMapping("listVO")
    public PageInfo<UserVO> list(@RequestBody PageRequest<ExtUserQuery> request) {
        PageInfo<UserVO> pageResult = dingUserQueryService.listPage(request);
        return pageResult;
    }

    @GetMapping("getVO")
    public UserVO list(@RequestParam Integer id) {
        return dingUserQueryService.getVO(id);
    }

}
