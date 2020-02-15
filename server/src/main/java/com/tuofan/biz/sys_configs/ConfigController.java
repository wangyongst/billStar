package com.tuofan.biz.sys_configs;

import com.google.common.collect.Lists;
import com.tuofan.biz.sys_configs.constants.ConfigNameConstants;
import com.tuofan.biz.sys_configs.entity.SystemConfig;
import com.tuofan.biz.sys_configs.service.ConfigCachedUtils;
import com.tuofan.core.advice.auth.LoginRequired;
import com.tuofan.core.advice.convert.FieldConversion;
import com.tuofan.core.exception.BizException;
import com.tuofan.core.utils.DateTimeUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@Controller
@RequestMapping("${cert.api.prefix}/config")
@Slf4j
public class ConfigController {

    @Autowired
    ConfigCachedUtils configCachedUtils;

    @GetMapping("list")
    @LoginRequired
    @FieldConversion
    public List<SystemConfig> list() {
        return configCachedUtils.listAll();
    }

    @GetMapping("getByName")
    @LoginRequired
    @ApiOperation("getByName")
    public SystemConfig getByName(@RequestParam String name) {
        return configCachedUtils.getItem(name);
    }

    @PostMapping("update")
    @LoginRequired
    @ApiOperation("传id和value到json的body")
    public Integer update(@RequestBody SystemConfig item) {
        List<String> timeFields = Lists.newArrayList(ConfigNameConstants.noticeSchoolYearBeginTime, ConfigNameConstants.reportSchoolYearBeginTime);
        if (timeFields.contains(item.getName())) {
            try {
                Date date = DateTimeUtils.getFormatDate(item.getValue(), DateTimeUtils.DATE_FORMAT_DEFAULT);
                if (date == null) {
                    throw new BizException("101", "时间格式不正确,请按格式 【yyyy-MM-dd HH:mm:ss】输入");
                }
            } catch (Exception e) {
                throw new BizException("101", "时间格式不正确，请按格式 【yyyy-MM-dd HH:mm:ss】输入");
            }
        }
        return configCachedUtils.update(item);
    }


    @GetMapping("refresh")
    @LoginRequired
    @ApiOperation("刷新")
    public List<SystemConfig> refresh() {
        configCachedUtils.refreshConfigCache();
        return configCachedUtils.listAll();
    }

    @GetMapping("getDateByName")
    @LoginRequired
    @ApiOperation("getDateByName")
    public Date getDateByName(@RequestParam String name) {
        SystemConfig systemConfig = configCachedUtils.getItem(name);
        if (systemConfig == null) {
            return null;
        }
        return DateTimeUtils.getFormatDate(systemConfig.getValue());
    }
}
