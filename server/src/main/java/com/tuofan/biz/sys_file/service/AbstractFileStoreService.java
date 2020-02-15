package com.tuofan.biz.sys_file.service;

import com.tuofan.core.utils.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractFileStoreService implements FileStoreService {
    /**
     * 创建服务器用来存放该文件时用的
     *
     * @param fileName 文件，名字
     * @return 名字
     */
    protected String createFileNameForStore(String fileName) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmSS");
        String format = formatter.format(new Date());
        StringBuffer buffer = new StringBuffer(format);
        int iTemp = fileName.hashCode();
        if (iTemp != Integer.MIN_VALUE) {
            buffer.append(Math.abs(iTemp));
        } else {
            buffer.append(iTemp);
        }

        buffer.append(RandomStringUtils.random(4, 0, 0, false, true, (char[]) null, ThreadLocalRandom.current()));
        buffer.append(fileName.substring(fileName.lastIndexOf(".")));
        return buffer.toString();
    }

    /**
     * 获取相对路径
     *
     * @param group
     * @return
     */
    protected String getRelativeDir( String group) {
        StringBuilder builder = new StringBuilder();

        if (StringUtils.isNotEmpty(group)) {
            String trim = group.trim();
            if (!trim.startsWith("/")) {
                builder.append("/");
            }
            builder.append(trim);
            if (!trim.endsWith("/")) {
                builder.append("/");
            }
        } else {
            builder.append("/");
        }
        return builder.toString();
    }
}
