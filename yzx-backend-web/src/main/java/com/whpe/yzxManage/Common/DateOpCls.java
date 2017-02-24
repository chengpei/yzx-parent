package com.whpe.yzxManage.Common;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateOpCls {

	public static String DateToFormat(String date,int type){
		java.sql.Date temp=java.sql.Date.valueOf(date);	
		SimpleDateFormat df =null;
		switch(type){
			case 0: //2000-01-01
				df=   new SimpleDateFormat( "yyyyMMdd");
				break;
			case 1: //2000/01/01
				df=   new SimpleDateFormat( "yyyy/MM/dd");
				break;
			case 2: //2000/01/01
				df=   new SimpleDateFormat( "yyyy-MM-dd");
				break;
			case 5: //2000/01/01
				df=   new SimpleDateFormat( "yyyy年MM月dd日");
				break;
			case 6: //2000/01/01
				df=   new SimpleDateFormat( "yyyy-MM-dd HH:mm");
				break;
			case 7: //2000/01/01
				df=   new SimpleDateFormat( "yyyy-M-d");
				break;
			default:
				df=   new SimpleDateFormat( "yyyy-MM-dd");
				break;
		}		
		return df.format(temp);		
	}
	
	public static String DateToFormat(java.util.Date date,int type){
		SimpleDateFormat df =null;
		switch(type){
			case 0: //2000-01-01
				df=   new SimpleDateFormat( "yyyyMMdd");
				break;
			case 1: //2000/01/01
				df=   new SimpleDateFormat( "yyyy/MM/dd");
				break;
			case 2: //2000/01/01
				df=   new SimpleDateFormat( "yyyy-MM-dd");
				break;
			case 3: //2000/01/01
				df=   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
				break;
			case 5: //2000/01/01
				df=   new SimpleDateFormat( "yyyy年MM月dd日");
				break;
			case 15: //2000/01/01
				df=   new SimpleDateFormat( "yyyy年MM月dd日    HH时mm分ss秒");
				break;
			case 16: //2000/01/01
				df=   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
				break;
			case 7: //2000-01-01
				df=   new SimpleDateFormat( "yyyyMMdd");
				break;
			default:
				df=   new SimpleDateFormat( "yyyy-MM-dd");
				break;
		}		
		if(date==null)return "";
		return df.format(date);	
	}
	
	public static String TodayAndTime(int type){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat simform=null;
		switch(type){
		case 0: //2000-01-01
			simform=   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			break;
		case 1: //2000/01/01
			simform=   new SimpleDateFormat( "yyyy/MM/dd");
			break;
		case 2: //2000/01/01
			simform=   new SimpleDateFormat( "yyyy-MM-dd");
			break;
		case 9: //2000-01-01
			simform=   new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
			break;
		case 10: //2000/01/01
			simform=   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS");
			break;
		default:
			simform= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			break;
		}		
		 
		return simform.format(cal.getTime()); 
	}
	
	//获取昨天的日期格式
	public static String YestodayAndTime(int type){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		SimpleDateFormat simform=null;
		switch(type){
		case 0: //2000-01-01
			simform=   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			break;
		case 1: //2000/01/01
			simform=   new SimpleDateFormat( "yyyy/MM/dd");
			break;
		case 2: //2000/01/01
			simform=   new SimpleDateFormat( "yyyy-MM-dd");
			break;
		case 10: //2000/01/01
			simform=   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS");
			break;
		default:
			simform= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			break;
		}		
		 
		return simform.format(cal.getTime()); 
	}
	
	public static int DateCompare(int year1,int month1,int year2,int month2){
		if(year2>year1){
			return -1;
		}
		if(month2>month1){
			return -1;
		} else if(month2==month1){
			return 0;
		}else{
			return 1;
		}
	}
	
	public static int DateCompareToday(int year1,int month1){
		Calendar cal1 = Calendar.getInstance();
		cal1.set(year1,month1, 1);
		Calendar today = Calendar.getInstance();
		return cal1.compareTo(today);
		
	}
	
	public static int MonthSpan(Calendar ca1,Calendar ca2){
		return (ca1.get(Calendar.MONTH) -ca2.get(Calendar.MONTH))+12*(ca1.get(Calendar.YEAR) -ca2.get(Calendar.YEAR));
	}
	
	/**
	 * 获取当前日期的天数
	 * @param cal
	 * @return
	 */
	public static int getDays( Calendar cal) {
        Calendar a = (Calendar)cal.clone();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;  
    }  
	
}
