package com.back.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.back.common.constant.CommonConstant;
import io.swagger.models.auth.In;

public class DateUtil {

	static long time = 0;

	// 返回格式：xxxx-xx-xx
	public static String toFormatRdid(String rdid) {
		return rdid.substring(CommonConstant.ZERO, CommonConstant.FOUR) + "-"
				+ rdid.substring(CommonConstant.FOUR, CommonConstant.SIX) + "-"
				+ rdid.substring(CommonConstant.SIX, CommonConstant.EIGHT);
	}

	// 返回格式：xxxx-xx-xx
	public static String getRdidFlmat() {
		String rdid = getRdid();
		return rdid.substring(CommonConstant.ZERO, CommonConstant.FOUR) + "-"
				+ rdid.substring(CommonConstant.FOUR, CommonConstant.SIX) + "-"
				+ rdid.substring(CommonConstant.SIX, CommonConstant.EIGHT);
	}

	// 返回格式：xxxxxxxx
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

	//判断现在是否在开盘时间
	public static boolean open(){
		Date date = new Date();
		int hours = date.getHours();
		int minutes = date.getMinutes();
		//开盘时间
		if (hours > 9 && hours <= 15 ){
			//集合竞价不算开盘
			if (hours == 9 && minutes < 25){
				return false;
			}
			return true;
		}
		//未开盘
		return false;
	}

	/**
	 * 缓冲一小时
	 * @return
	 */
	public static boolean oneHours(){
		//第一次请求
		if (time == 0){
			time = System.currentTimeMillis()/1000;
			return true;
		}else {
			long diff = System.currentTimeMillis() / 1000 - time;
			if (diff > 3600){
				//重新开始计算
				time = System.currentTimeMillis()/1000;
				return true;
			}else {
				return false;
			}
		}
	}


	//根据时间戳，返回格式时间 秒
	public static String getDateByS(Integer time){
		long lt = new Long(time.toString()+"000");;
		Date date = new Date(lt);
		DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		String format = dateFormat.format(date);
		return format;
	}

	//根据时间戳，返回格式时间 毫秒
	public static String getDateByMs(Long time){
		Date date = new Date(time);
		DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		String format = dateFormat.format(date);
		return format;
	}
}
