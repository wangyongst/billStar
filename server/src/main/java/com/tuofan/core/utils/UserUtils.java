package com.tuofan.core.utils;

import com.tuofan.core.BizException;
import com.tuofan.core.LoginConstants;
import com.tuofan.basic.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
public class UserUtils {

    public static UserVO getLoginUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes())
                .getRequest();
        Object obj = request.getAttribute(LoginConstants.DING_USER);
        if (obj == null) {
            log.error("没能从request里面取到用户");
            return null;
        }
        return (UserVO) obj;
    }

    /**
     * 当前登录人校区ID信息
     *
     * @return
     */
    public static List<Integer> getLoginUserSchoolIds() {
        UserVO dingUser = getLoginUser();
        if (dingUser == null) {
            throw new BizException("SYS001", "无法定位到登录用户的校区信息");
        }
        return dingUser.getDeptSchoolIds();
    }
}
