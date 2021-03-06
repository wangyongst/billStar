package com.tuofan.core;

public class CheckUtils {

    public static boolean isEmpty(Long l) {
        if (l == null) return true;
        return false;
    }

    public static boolean isEmpty(Integer integer) {
        if (integer == null) return true;
        return false;
    }

    public static boolean isZero(Integer integer) {
        if (isEmpty(integer) || integer == 0) return true;
        return false;
    }

    public static boolean isZero(Long l) {
        if (isEmpty(l) || l == 0l) return true;
        return false;
    }

    public static boolean isNotEmpty(Integer integer) {
        return !isEmpty(integer);
    }

    public static boolean isNotZero(Integer integer) {
        return !isZero(integer);
    }

    public static boolean isNotZero(Long l) {
        return !isZero(l);
    }
}
