package com.personal.utils;


/**
 * <p>分页工具类</p>
 * @author
 */
public  final class PageUtil {

    public static void init(int page) {
        if (!StringUtil.isNullOrEmpty(page) && page > 0) {
            page = page;
        } else {
            page = 1;
        }
    }

}
