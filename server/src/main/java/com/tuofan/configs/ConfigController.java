package com.tuofan.configs;

import com.tuofan.core.Result;
import com.tuofan.configs.entity.SysConfigs;
import com.tuofan.configs.service.ConfigCachedUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bill/config")
@Slf4j
public class ConfigController {

    @Autowired
    private ConfigCachedUtils configCachedUtils;

    @GetMapping("list")
    public Result list() {
        return Result.ok(configCachedUtils.listAll());
    }

    @GetMapping("getByName")
    public Result getByName(@RequestParam String name) {
        return Result.ok(configCachedUtils.getItem(name));
    }

    @PostMapping("update")
    public Result update(@RequestBody SysConfigs item) {
        configCachedUtils.saveOrUpdate(item);
        return Result.ok();
    }

    @GetMapping("refresh")
    public Result refresh() {
        configCachedUtils.refreshConfigCache();
        return Result.ok(configCachedUtils.listAll());
    }
}
