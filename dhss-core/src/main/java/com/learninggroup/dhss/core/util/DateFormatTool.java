package com.learninggroup.dhss.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * <li>功能描述：用于操作格式化时间的工具类
 * @author 高俊
 *
 */
public class DateFormatTool {
	
	public static final SimpleDateFormat[] formatArray = new SimpleDateFormat[]{
		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),new SimpleDateFormat("yyyy-MM-dd")
		,new SimpleDateFormat("yyyy年MM月dd�? HH时mm分ss�?"),new SimpleDateFormat("yyyy年MM月dd�?")
		,new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"),new SimpleDateFormat("yyyy/MM/dd")
		,new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy",Locale.US)};
	
	/**
	 * 转化java.sql.Date�?"yyyy-MM-dd"格式
	 * @param java.sql.Date
	 * @return
	 */
	public static String sqlDateToString(java.sql.Date date){
		String dateString = "";
		if(date != null){
			dateString = date.toString();
		}
		return dateString;
	}
	
	/**
	 * SQL日期转化为日�?
	 * @param date java.sql.Date
	 * @return
	 * 		java.util.Date 
	 */
	public static java.util.Date sqlDateToDate(java.sql.Date date){
		if(date != null){
			long a = date.getTime();
			java.util.Date d1 = new java.util.Date(a);
			return d1;
		}else{
			return null;
		}
	}
	
	/**
	 * 日期转化为SQL日期
	 * @param date java.util.Date
	 * @return
	 * 		java.sql.Date
	 */
	public static java.sql.Date dateToSqlDate(java.util.Date date){
		if(date != null){
			long a = date.getTime();
			java.sql.Date d1 = new java.sql.Date(a);
			return  d1;
		}else{
			return null;
		}
	}
	
	/**
	 * 转化java.util.Date�?"yyyy-MM-dd HH:mm:ss"格式
	 * @param date
	 * @return
	 */
	public static String dateToString(java.util.Date date){
		String str = "";
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			str = formate.format(date);
		}catch (Exception e) {
			
		}
		return str;
	}
	
	/**
	 * 转化java.util.Date�?"yyyy-MM-dd"格式
	 * @param date
	 * @return
	 */
	public static String dateToStringY(java.util.Date date){
		String str = "";
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
		try{
			str = formate.format(date);
		}catch (Exception e) {
			
		}
		return str;
	}
	
	/**
	 * 把java.util.Date 转化成yyyy年MM月dd�?
	 * @param date
	 * @return
	 */
	public static String dateToStringZh(java.util.Date date){
		String str = "";
		SimpleDateFormat formate = new SimpleDateFormat("yyyy年MM月dd�?");
		try{
			str = formate.format(date);
		}catch (Exception e) {
			
		}
		return str;
	}

	/**
	 * 根据指定的format把字符串转换成日�?
	 * @param dataStr
	 * @param format
	 * @return
	 */
	public static Date stringToDate(String dataStr , SimpleDateFormat format){
		Date date = null;
		try{
			date = format.parse(dataStr);
		}catch (Exception e) {
			date = null;
		}
		return date;
	}
	
	/**
	 * 把字符串转换成日�?
	 * @param dateStr
	 * @return
	 */
	public static Date stringToDate(String dateStr){
		Date date = null;
		for(SimpleDateFormat format : formatArray){
			try {
				date = format.parse(dateStr);
				break;
			} catch (ParseException e) {
			}
		}
		return date;
	}
	
	/**
	 * 返回两个日期之间的天�?
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static Long getDates(Date beginDate , Date endDate){
		if(beginDate == null || endDate == null)
			return null;
		long time1 = beginDate.getTime();
		long time2 = endDate.getTime();
		Long days = (time2-time1)/(1000*60*60*24);
		if(days < 0){
			days = 0l;
		}
		return days;
		
	}
	
	/**
	 * 根据�?始时间，结束时间，判断在这个时间跨度内，指定月占的天�?
	 * @param beginDate
	 * @param endDate
	 * @param month
	 * 		必须是yyyy-MM的格�?
	 * @return
	 */
	public static Long getDatesInMonth(Date beginDate , Date endDate , String month){
		long days = 0l;
		if(beginDate == null || endDate == null || month == null || "".equals(month))
			return 0l;
		
		//当月日期的两个端点为当月�?号到下月�?�?
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		Calendar c3 = Calendar.getInstance();
		Calendar c4 = Calendar.getInstance();
		
		//当月�?�?
		String ond = month+"-"+"01";
		Date onDate = stringToDate(ond);
		c1.setTime(onDate);
		//下月�?�?
		c2.setTime(onDate);
		c2.add(Calendar.MONTH, 1);
		//�?始时�?
		c3.setTime(beginDate);
		//结束时间
		c4.setTime(endDate);
		
		//判断�?始时间，结束时间跨度
		//如果�?始时�?<=当前月的第一天，结束时间>=下个月的第一天，说明时间跨度上这个月占满天数
		if((c3.equals(c1)||c3.before(c1)) && (c4.equals(c2)||c4.after(c2))){
			days = getDates(c1.getTime(), c2.getTime());
		}else if((c3.equals(c1)||c3.before(c1)) && (c4.equals(c2)||c4.before(c2))){
			days = getDates(c1.getTime(), c4.getTime());
		}else if((c3.equals(c1)||c3.after(c1)) && (c4.equals(c2)||c4.after(c2))){
			days = getDates(c3.getTime(), c2.getTime());
		}else if((c3.equals(c1)||c3.after(c1)) && (c4.equals(c2)||c4.before(c2))){
			days = getDates(c3.getTime(), c4.getTime());
		}
		
		
		return days;
	}
	
	/**
	 * 返回两个日期之间的所有月�?
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static List<String> getMothsBetweenTwoDay(Date beginDate , Date endDate){
		if(beginDate == null || endDate == null){
			return null;
		}
		
		String str = null;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		c1.setTime(beginDate);
		c2.setTime(endDate);
		
		if(c1.after(c2)){
			c1.setTime(endDate);
			c2.setTime(beginDate);
		}
		List<String> list = new ArrayList<String>();
		
		while(c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR)|| c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH)){
			str = dateToStringY(c1.getTime());
			str = str.substring(0,str.lastIndexOf("-"));
			list.add(str);
			c1.add(Calendar.MONTH, 1);
		}
		
		str = dateToStringY(c1.getTime());
		str = str.substring(0,str.lastIndexOf("-"));
		list.add(str);
		
		return list;
	}
	
	/**
	 * 返回两个日期之间的所有季�?
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static List<QuarterBO> getQuarterBetweenTwoDay(Date beginDate , Date endDate){
		if(beginDate == null || endDate == null){
			return null;
		}
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		c1.setTime(beginDate);
		c2.setTime(endDate);
		
		if(c1.after(c2)){
			c1.setTime(endDate);
			c2.setTime(beginDate);
		}
		
		List<QuarterBO> list = new ArrayList<QuarterBO>();
		
		
		while(c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR)
				|| c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH)){
			//计算季度
			initQuarte(c1, list);
			c1.add(Calendar.MONTH, 1);
		}
		
		//计算季度
		initQuarte(c1, list);
		return list;
	}
	
	private static void initQuarte(Calendar c1 ,List<QuarterBO> list){
		
		//�?始的年份
		int year = 0;
		//�?始的季度
		int quarte = 0;
		
		year = c1.get(Calendar.YEAR);
		//当前月份
		int month = c1.get(Calendar.MONTH)+1;
		
		if(month == 1 || month == 11 || month == 12){
			if(month == 1){
				//如果�?2014�?1月，则为2013年第4季度
				//年份�?1
				year = year - 1;
			}
			quarte = 4;
		}else if(month == 2 || month == 3 || month == 4){
			quarte = 1;
		}else if(month == 5 || month == 6 || month == 7){
			quarte = 2;
		}else{
			quarte = 3;
		}
		
		if(list != null && list.size() > 0){
			QuarterBO preBo = list.get(list.size()-1);
			if(preBo.getYear() == year && preBo.getQuarte() == quarte){
				//如果list中已经有了同�?个季度的信息，那么不再添�?
			}else{
				QuarterBO bo = new QuarterBO();
				bo.setYear(year);
				bo.setQuarte(quarte);
				list.add(bo);
			}
		}else{
			QuarterBO bo = new QuarterBO();
			bo.setYear(year);
			bo.setQuarte(quarte);
			list.add(bo);
		}
		
	}
	
	/**
	 * 返回在beginDate到endDate这一时间跨度内经过指定季度的天数
	 * @param begiDate
	 * @param endDate
	 * @param quarterBO
	 * @return
	 */
	public static Long getDaysInQuarte(Date beginDate , Date endDate , QuarterBO quarterBO){
		if(beginDate == null || endDate == null || quarterBO == null){
			return null;
		}
		
		Long days = 0l;
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		Calendar c3 = Calendar.getInstance();
		Calendar c4 = Calendar.getInstance();
		
		Date quBeginDate = quarterBO.getBeginDate();
		Date quEndDate = quarterBO.getEndDate();
		c1.setTime(quBeginDate);
		c2.setTime(quEndDate);
		c3.setTime(beginDate);
		c4.setTime(endDate);
		
		//判断�?始时间，结束时间跨度
		//如果�?始时�?<=当季度的第一天，结束时间>=下个季度的第�?天，说明时间跨度上这个季度占满天�?
		if((c3.equals(c1)||c3.before(c1)) && (c4.equals(c2)||c4.after(c2))){
			days = getDates(c1.getTime(), c2.getTime());
		}else if((c3.equals(c1)||c3.before(c1)) && (c4.equals(c2)||c4.before(c2))){
			days = getDates(c1.getTime(), c4.getTime());
		}else if((c3.equals(c1)||c3.after(c1)) && (c4.equals(c2)||c4.after(c2))){
			days = getDates(c3.getTime(), c2.getTime());
		}else if((c3.equals(c1)||c3.after(c1)) && (c4.equals(c2)||c4.before(c2))){
			days = getDates(c3.getTime(), c4.getTime());
		}
		
		return days;
	}
	
	/**
	 * 返回当前年度
	 * @return
	 */
	public static int getThisYear(){
		Calendar c1 = Calendar.getInstance();
		return c1.get(Calendar.YEAR);
	}
	
}
