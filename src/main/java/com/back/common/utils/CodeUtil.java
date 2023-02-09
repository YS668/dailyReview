package com.back.common.utils;

import com.back.common.constant.CrawConstant;
import com.back.entity.pojo.Up;

/**
 * 股票代码工具
 */
public class CodeUtil {

    //SH600586 -- sh600586
    public static String toLow( String stockCode){
        return stockCode.substring(0,2).toLowerCase()+stockCode.substring(2);
    }

    //sh600586 -- SH600586
    public static String toUp( String stockCode){
        return stockCode.substring(0,2).toUpperCase()+stockCode.substring(2);
    }

    //600586 -- SH600586
    public static String numToCode( String stockCode){
        if (stockCode.startsWith("0") || stockCode.startsWith("3")){
            return CrawConstant.SZ+stockCode;
        }else if (stockCode.startsWith("6")){
            return CrawConstant.SH+stockCode;
        }
        return null;
    }

}
