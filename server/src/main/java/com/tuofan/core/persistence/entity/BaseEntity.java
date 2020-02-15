package com.tuofan.core.persistence.entity;

import com.tuofan.core.constants.LoginConstants;
import lombok.Data;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;

/**
 * 默认字段 id createDate updateDate createBy
 */
@Data
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private Date createDate;

    private Date updateDate;

    private Integer createBy;

    private Integer updateBy;

    public void preInsert() {
        this.createDate = new Date();
        this.updateDate = new Date();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Integer createBy = request.getIntHeader(LoginConstants.USER_ID);
        this.setCreateBy(createBy);
    }

    public void preUpdate() {
        this.updateDate = new Date();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Integer updateBy = request.getIntHeader(LoginConstants.USER_ID);
        this.setUpdateBy(updateBy);
    }
}
