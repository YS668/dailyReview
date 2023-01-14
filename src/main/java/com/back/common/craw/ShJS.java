package com.back.common.craw;

import java.util.*;

/**
 * 上交所股票js文件接口
 */
public interface ShJS {

    /**
     * 获取所有股票
     * @return
     */
    List<Map<String,String>> get_data();
}
