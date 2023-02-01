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
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		if (Integer.valueOf(month) < CommonConstant.TEN){
			month = CommonConstant.ZERO+""+month;
		}
		String date = String.valueOf(c.get(Calendar.DATE));
		if (Integer.valueOf(date) < CommonConstant.TEN){
			date = CommonConstant.ZERO+""+date;
		}
		return year + "" + month + "" + date;
	}
}
