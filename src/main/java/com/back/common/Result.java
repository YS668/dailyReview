package com.back.common;

import lombok.Data;

@Data
public class Result {

    private static final int FAIL_CODE = 400;
    private static final String FAIL_MSG = "失败";

    private static final int SUC_CODE = 200;
    private static final String SUC_MSG = "成功";

    private static final int TOKEN_ERROR_CODE = 401;
    private static final String TOKEN_ERROR_MSG = "请重新登录";

    private static final long ZERO  = 0L;

    public static final String MAIL_REPEAT  = "存在相同的邮箱";

    //编码
    private int code;
    //成功或者失败
    private String msg;
    //总记录数
    private Long total;
    //数据
    private Object data;

    public static Result tokenError(){
        return result(Result.TOKEN_ERROR_CODE,Result.TOKEN_ERROR_MSG,Result.ZERO,null);
    }

    public static Result fail(){
        return result(Result.FAIL_CODE,Result.FAIL_MSG,Result.ZERO,null);
    }

    public static Result fail(String msg){
        return result(Result.FAIL_CODE,msg,Result.ZERO,null);
    }

    public static Result suc(Object data){
        return result(Result.SUC_CODE,Result.SUC_MSG,Result.ZERO,data);
    }

    public static Result suc(){
        return result(Result.SUC_CODE,Result.SUC_MSG,Result.ZERO,null);
    }

    public static Result suc(Long total, Object data){
        return result(Result.SUC_CODE,Result.SUC_MSG,total,data);
    }

    public static Result result(int code, String msg, Long total, Object data) {
        Result res = new Result();
        res.code = code;
        res.msg = msg;
        res.total = total;
        res.data = data;
        return res;
    }
}