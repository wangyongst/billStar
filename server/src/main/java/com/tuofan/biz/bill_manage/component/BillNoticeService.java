package com.tuofan.biz.bill_manage.component;

import com.tuofan.biz.bill_manage.service.BillQueryService;
import com.tuofan.biz.bill_manage.vo.SaleDTO;
import com.tuofan.biz.bill_manage.vo.SaleTimeQuery;
import com.tuofan.biz.sys_configs.constants.ConfigNameConstants;
import com.tuofan.biz.sys_configs.service.ConfigCachedUtils;
import com.tuofan.biz.sys_ding.request.RobotMessageSendRequest;
import com.tuofan.biz.sys_ding.request.param.robot.messsage.MarkDownMessageDTO;
import com.tuofan.biz.sys_ding.response.base.BaseResponse;
import com.tuofan.biz.sys_orgnization.domain.entity.DingDept;
import com.tuofan.biz.sys_orgnization.domain.service.DingDeptRepository;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.dto.BaseResult;
import com.tuofan.core.utils.DateTimeUtils;
import com.tuofan.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BillNoticeService {

    @Autowired
    private BillQueryService billQueryService;

    @Autowired
    private ConfigCachedUtils configCachedUtils;

    @Autowired
    private RobotMessageSendRequest robotMessageSendRequest;

    @Autowired
    private DingDeptRepository dingDeptRepository;


    public BaseResult triggerNotice() {
        try {
            String schoolYearBeginTimeStr = configCachedUtils.getValue(ConfigNameConstants.noticeSchoolYearBeginTime);
            if (StringUtils.isEmpty(schoolYearBeginTimeStr)) {
                return BaseResult.buildFailResult("发送群通知失败，没有配置学年开始时间。");
            }
            Date schoolYearBeginTime = DateTimeUtils.getFormatDate(schoolYearBeginTimeStr);
            String managerRobotURL = configCachedUtils.getValue(ConfigNameConstants.managerRobotURL);
            if (StringUtils.isEmpty(managerRobotURL)) {
                return BaseResult.buildFailResult("发送群通知失败，没有配置管理群机器人地址。");
            }
            // 做查询
            List<SaleDTO> todaySales = billQueryService.listSaleDTO(SaleTimeQuery.ofTodayInstance());
            List<SaleDTO> monthSales = billQueryService.listSaleDTO(SaleTimeQuery.ofCurrrentMonthInstance());
            List<SaleDTO> yearSales = billQueryService.listSaleDTO(SaleTimeQuery.ofCurrentYearInstance(schoolYearBeginTime));
            // 组成map
            Map<Integer, SaleDTO> todaySaleMap = todaySales.stream().collect(Collectors.toMap(SaleDTO::getDeptSchoolId, x -> x));
            Map<Integer, SaleDTO> monthSaleMap = monthSales.stream().collect(Collectors.toMap(SaleDTO::getDeptSchoolId, x -> x));
            Map<Integer, SaleDTO> yearSaleMap = yearSales.stream().collect(Collectors.toMap(SaleDTO::getDeptSchoolId, x -> x));
            // 校区
            List<DingDept> schoolZones = dingDeptRepository.listSchoolZone();
            StringBuilder noticeContentBuilder = new StringBuilder(generateTitle());
            for (DingDept schoolZone : schoolZones) {
                noticeContentBuilder.append(this.buildMarkDownNoticeContent(schoolZone, todaySaleMap, monthSaleMap, yearSaleMap));
            }
            noticeContentBuilder.append(this.buildMarkDownNoticeContent(-1, "合计", todaySaleMap, monthSaleMap, yearSaleMap));
            // 开始发通知
            MarkDownMessageDTO markDownMessageDTO = MarkDownMessageDTO.of("云校管销售汇总", noticeContentBuilder.toString());
            BaseResponse baseResponse = robotMessageSendRequest.sendMessage(managerRobotURL, markDownMessageDTO);
            if (baseResponse.getErrcode() != BaseResponse.OK) {
                log.error("日报月报年报-消息发送失败，原因：请求钉钉失败，error={}", baseResponse.getErrmsg());
                return BaseResult.buildFailResult(baseResponse.getErrmsg());
            }
            return BaseResult.buildSuccessResult();
        } catch (Exception e) {
            log.error("triggerNotice e=", e);
            return BaseResult.buildFailResult(e.getCause().getLocalizedMessage() + e.getMessage());
        }
    }

    private String generateTitle() {
        return "##### 今日报表  " + AppConstants.markDownNextLine + " *** " + AppConstants.markDownNextLine;
    }

    private String buildMarkDownNoticeContent(DingDept schoolZone, Map<Integer, SaleDTO> todaySaleMap, Map<Integer, SaleDTO> monthSaleMap, Map<Integer, SaleDTO> yearSaleMap) {
        return this.buildMarkDownNoticeContent(schoolZone.getId(), schoolZone.getName(), todaySaleMap, monthSaleMap, yearSaleMap);

    }

    private String buildMarkDownNoticeContent(Integer key, String title, Map<Integer, SaleDTO> todaySaleMap, Map<Integer, SaleDTO> monthSaleMap, Map<Integer, SaleDTO> yearSaleMap) {
        StringBuilder stringBuilder = new StringBuilder("#### <font color=#D72638>")
                .append(title)
                .append("</font>")
                .append(AppConstants.markDownNextLine);
        SaleDTO todaySale = todaySaleMap.get(key);
        SaleDTO monthSale = monthSaleMap.get(key);
        SaleDTO yearSale = yearSaleMap.get(key);
        stringBuilder.append(processOneLine("今日", todaySale == null ? 0.0 : todaySale.getAmount()));
        stringBuilder.append(processOneLine("本月", monthSale == null ? 0.0 : monthSale.getAmount()));
        stringBuilder.append(processOneLine("今年", yearSale == null ? 0.0 : yearSale.getAmount()));
        return stringBuilder.toString();
    }

    private String processOneLine(String name, Double value) {
        return new StringBuilder(">")
                .append(name)
                .append(" : ")
                .append(value)
                .append(AppConstants.markDownNextLine).toString();
    }


}
