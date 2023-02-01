package com.back.common.utils;

import java.util.Calendar;

import com.back.common.constant.CommonConstant;

public class DateUtil {

	// 返回格式：xxxx-xx-xx
	public static String getByRdid(String rdid) {
		return rdid.substring(CommonConstant.ZERO, CommonConstant.FOUR) + "-"
				+ rdid.substring(CommonConstant.FOUR, CommonConstant.SIX) + "-"
				+ rdid.substring(CommonConstant.SIX, CommonConstant.EIGHT);
	}

	// 返回格式：xxxx-xx-xx
	public static String getRdid() {
		// 日期
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int date = c.get(Calendar.DATE);
		return year + "" + month + "" + date;
	}
}
