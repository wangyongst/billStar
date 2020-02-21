package com.tuofan.core;


import com.google.common.collect.Lists;
import com.tuofan.core.utils.SysBeanUtils;

import java.util.List;

/**
 * 字段相同的转换可以直接使用这个帮助类，慎用
 */
public class ModelConvertHelper {

    /**
     * bean convert
     *
     * @param orig      源
     * @param destClass 目标
     * @param <ORIG>
     * @param <DEST>
     * @return
     */
    public static <ORIG, DEST> DEST convert(ORIG orig, Class<DEST> destClass) {
        try {
            if (orig == null) {
                return destClass.newInstance();
            }
            DEST dest = destClass.newInstance();
            SysBeanUtils.copyProperties(dest, orig);
            return dest;
        } catch (Exception e) {
            throw new BizException(e);
        }
    }

    /**
     * bean list convert ,dest & origin should have the same fields
     *
     * @param orig      数据
     * @param destClass 目标数据
     * @param <ORIG>
     * @param <DEST>
     * @return
     */
    public static <ORIG, DEST> List<DEST> convertList(List<ORIG> orig, Class<DEST> destClass) {
        List<DEST> dests = Lists.newArrayList();
        if (orig == null) {
            return dests;
        }

        for (ORIG o : orig) {
            DEST d = convert(o, destClass);
            dests.add(d);
        }
        return dests;
    }
}