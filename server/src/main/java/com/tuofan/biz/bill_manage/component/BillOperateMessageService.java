package com.tuofan.biz.bill_manage.component;

import com.google.common.collect.Lists;
import com.tuofan.biz.bill_manage.constants.BillTypes;
import com.tuofan.biz.bill_manage.service.BillQueryService;
import com.tuofan.biz.bill_manage.vo.BillCourseVO;
import com.tuofan.biz.bill_manage.vo.BillVO;
import com.tuofan.biz.sys_ding.request.RobotMessageSendRequest;
import com.tuofan.biz.sys_ding.request.param.robot.messsage.MarkDownMessageDTO;
import com.tuofan.biz.sys_ding.response.base.BaseResponse;
import com.tuofan.biz.sys_orgnization.domain.entity.DingDept;
import com.tuofan.biz.sys_orgnization.domain.service.DingDeptRepository;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BillOperateMessageService {

    @Autowired
    BillQueryService billQueryService;

    @Autowired
    DingDeptRepository dingDeptRepository;

    @Autowired
    RobotMessageSendRequest robotMessageSendRequest;



    public void processBillMessage(Integer billId) {
        try {
            BillVO billVO = billQueryService.getVO(billId);
            if (billVO == null) {
                log.error("开票操作-消息发送失败,billId={}。原因：没有根据ID找到Bill实体", billId);
                return;
            }
            // 只处理新生，续费
            if (!Lists.newArrayList(BillTypes.NEW_BILL, BillTypes.RENEWALS).contains(billVO.getType())) {
                return;
            }
            DingDept dingDept = dingDeptRepository.get(billVO.getDeptSchoolId());
            if (dingDept == null || StringUtils.isBlank(dingDept.getGroupWebHook())) {
                log.error("开票操作-消息发送失败，billId={}。原因：没有找到校区或者，校区没有配置机器人地址", billId);
                return;
            }
            String title = generateMarkdownTitle(billVO.getType());
            String content = generateMarkdownContent(billVO);
            String message = title + AppConstants.markDownNextLine + content;
            MarkDownMessageDTO markDownMessageDTO = MarkDownMessageDTO.of("云校管消息提醒", message);
            BaseResponse baseResponse = robotMessageSendRequest.sendMessage(dingDept.getGroupWebHook(), markDownMessageDTO);
            if (baseResponse.getErrcode() != BaseResponse.OK) {
                log.error("开票操作-消息发送失败，billId={}。原因：请求钉钉失败，error={}", billId, baseResponse.getErrmsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("开票操作-消息发送失败，billId={}。原因：请求钉钉失败，error={}", billId, e.getMessage());
        }
    }

    private String generateMarkdownContent(BillVO billVO) {
        StringBuilder stringBuilder = new StringBuilder("##### 姓名：")
                .append(billVO.getStudent().getName())
                .append("  电话：")
                .append(billVO.getStudent().getMobile())
                .append(AppConstants.markDownNextLine);
        for (BillCourseVO billCourseVO : billVO.getCourses()) {
            String courseName = billCourseVO.getDictCourseName() + billCourseVO.getCourseIndexName();
            stringBuilder.append("> 课程：")
                    .append(courseName)
                    .append("  教师：")
                    .append(billCourseVO.getTeacherName())
                    .append(AppConstants.markDownNextLine);
        }
        return stringBuilder.toString();
    }

    private String generateMarkdownTitle(Integer type) {
        StringBuilder stringBuilder = new StringBuilder("#### ");
        if (type.equals(BillTypes.NEW_BILL)) {
            stringBuilder.append("<font color=#D72638>新生交费提醒</font>");
        } else if (type.equals(BillTypes.RENEWALS)) {
            stringBuilder.append("<font color=#00CC00>老生续费提醒</font>");
        }
        return stringBuilder.toString();
    }


}
