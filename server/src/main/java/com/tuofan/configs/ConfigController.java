package com.tuofan.configs;

import com.tuofan.configs.entity.SysConfigs;
import com.tuofan.configs.service.ISysConfigsService;
import com.tuofan.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bill/config")
@Slf4j
public class ConfigController {

//    @Autowired
//    private ConfigCachedUtils configCachedUtils;

    @Autowired
    private ISysConfigsService iSysConfigsService;

    @GetMapping("list")
    public Result list() {
        return Result.ok(iSysConfigsService.list());
    }

    @GetMapping("getByName")
    public Result getByName(@RequestParam String name) {
        return Result.ok(iSysConfigsService.findByName(name));
    }

    @PostMapping("update")
    public Result update(@RequestBody SysConfigs item) {
        SysConfigs sysConfigs = iSysConfigsService.findByName(item.getName());
        sysConfigs.setValue(item.getValue());
        iSysConfigsService.saveOrUpdate(sysConfigs);
        return Result.ok();
    }
}
