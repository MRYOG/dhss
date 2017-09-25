package com.learninggroup.dhss.core.util;

import java.math.BigDecimal;


/**
 * <li>功能描述：各种数据类型的转换
 * <li>null�?""统一转换�?0
 * <li>�?有为null的对像都统一转换�?""
 *@author 高俊
 */
public class DataFormatTool {
	
	private static final int DEF_DIV_SCALE = 10;

	/**
	 * 字符串转换成Long，如果isForNull为true则转换失败返回Null;如果isForNull为false，则转换失败返回0
	 * @param str
	 * @param isForNull
	 * @return
	 */
	public static Long stringToLong(String str , boolean isForNull){
		Long x = null;
		if(!isForNull){
			return stringToLong(str);
		}else{
			try{
				x = Long.valueOf(str);
			}catch (Exception e) {
			}
		}
		return x;
	}

	public static Long stringToLong(String str){
		Long x = null;
		try{
			str = str.trim();
			x = Long.valueOf(str);
		}catch (Exception e) {
			x = 0l;
		}
		return x;
	}
	
	public static String longToString(Long a){
		if(a == null)
			return "";
		return a.toString();
	}
	
	
	public static String integerToString(Integer a){
		if(a == null)
			return "";
		return a.toString();
	}
	
	/**
	 * 字符串转换成Integer，如果isForNull为true则转换失败返回Null;如果isForNull为false，则转换失败返回0
	 * @param str
	 * @param isForNull
	 * @return
	 */
	public static Integer stringToInteger(String str , boolean isForNull){
		Integer x = null;
		if(!isForNull){
			return stringToInteger(str);
		}else{
			try{
				x = Integer.valueOf(str);
			}catch (Exception e) {
			}
		}
		return x;
	}
	public static Integer stringToInteger(String str){
		Integer x = null;
		try{
			str = str.trim();
			x = Integer.valueOf(str);
		}catch (Exception e) {
			x = 0;
		}
		return x;
	}
	
	public static Integer longToInteger(Long a){
		if(a == null)
			return 0;
		String str = longToString(a);
		return stringToInteger(str);
	}
	
	public static Long integerToLong(Integer a){
		if(a == null)
			return 0l;
		String str = integerToString(a);
		return stringToLong(str);
	}
	
	/**
	 * 字符串转换成Integer，如果isForNull为true则转换失败返回Null;如果isForNull为false，则转换失败返回0
	 * @param str
	 * @param isForNull
	 * @return
	 */
	public static Double stringToDouble(String str , boolean isForNull){
		Double x = null;
		if(!isForNull){
			return stringToDouble(str);
		}else{
			try{
				x = Double.valueOf(str);
			}catch (Exception e) {
			}
		}
		return x;
	}
	public static Double stringToDouble(String str){
		double x;
		try{
			str = str.trim();
			x = Double.parseDouble(str);
		}catch (Exception e) {
			x = 0;
		}
		return x;
	}
	
	public static String doubleToString(Double dou){
		if(dou == null){
			return "";
		}
		return dou.toString();
	}
	
	public static Double longToDouble(Long a){
		String str = longToString(a);
		double x = stringToDouble(str);
		return x;
	}
	
	public static Double integerToDouble(Integer a){
		String str = integerToString(a);
		double x = stringToDouble(str);
		return x;
	}
	
	public static Long doubleToLong(Double a){
		String str = doubleToString(a);
		long x = stringToLong(str);
		return x;
	}
	
	public static Integer doubleToInteger(Double a){
		String str = doubleToString(a);
		int x = stringToInteger(str);
		return x;
	}
	
	public static Long objectToLong(Object obj){
		if(obj == null){
			return null;
		}
		
		return stringToLong(obj.toString());
	}
	
	public static Long objectToLong(Object obj , boolean isForNull){
		if(obj == null){
			return null;
		}
		
		return stringToLong(obj.toString() , isForNull);
	}
	
	public static Integer objectToInteger(Object obj , boolean isForNull){
		if(obj == null){
			return null;
		}
		
		return stringToInteger(obj.toString() , isForNull);
	}
	
	public static Integer objectToInteger(Object obj){
		if(obj == null){
			return null;
		}
		
		return stringToInteger(obj.toString());
	}
	
	public static Double objectToDouble(Object obj , boolean isForNull){
		if(obj == null){
			return null;
		}
		
		return stringToDouble(obj.toString() , isForNull);
	}
	
	public static Double objectToDouble(Object obj){
		if(obj == null){
			return null;
		}
		
		return stringToDouble(obj.toString());
	}
	
	public static String objectToString(Object ob){
		if(ob == null){
			return "";
		}
		
		return ob.toString();
	}
	
	public static Float objectToFloat(Object obj){
		if(obj == null){
			return null;
		}
		
		return stringToFloat(obj.toString());
	}

	private static Float stringToFloat(String str) {
		float x;
		try{
			str = str.trim();
			x = Float.parseFloat(str);
		}catch (Exception e) {
			x = 0;
		}
		return x;
	}
	
	public static Float objectToFloat(Object obj , boolean isForNull){
		if(obj == null){
			return null;
		}
		
		return stirngToFloat(obj.toString() , isForNull);
	}

	private static Float stirngToFloat(String str, boolean isForNull) {
		Float x = null;
		if(!isForNull){
			return stringToFloat(str);
		}else{
			try{
				x = Float.valueOf(str);
			}catch (Exception e) {
			}
		}
		return x;
	}
	
	 /**

     * 两个Double数相�?

     * @param v1

     * @param v2

     * @return Double

     */

    public static Double add(Double v1,Double v2){

        BigDecimal b1 = new BigDecimal(v1.toString());

        BigDecimal b2 = new BigDecimal(v2.toString());

        return b1.add(b2).doubleValue();

    }



    /**

     * 两个Double数相�?

     * @param v1

     * @param v2

     * @return Double

     */

    public static Double sub(Double v1,Double v2){

        BigDecimal b1 = new BigDecimal(v1.toString());

        BigDecimal b2 = new BigDecimal(v2.toString());

        return b1.subtract(b2).doubleValue();

    }



    /**

     * 两个Double数相�?

     * @param v1

     * @param v2

     * @return Double

     */

    public static Double mul(Double v1,Double v2){

        BigDecimal b1 = new BigDecimal(v1.toString());

        BigDecimal b2 = new BigDecimal(v2.toString());

        return b1.multiply(b2).doubleValue();

    }



    /**

     * 两个Double数相�?

     * @param v1

     * @param v2

     * @return Double

     */

    public static Double div(Double v1,Double v2){

        BigDecimal b1 = new BigDecimal(v1.toString());

        BigDecimal b2 = new BigDecimal(v2.toString());

        return b1.divide(b2,DEF_DIV_SCALE,BigDecimal.ROUND_HALF_UP).doubleValue();

    }



    /**

     * 两个Double数相除，并保留scale位小�?

     * @param v1

     * @param v2

     * @param scale

     * @return Double

     */

    public static Double div(Double v1,Double v2,int scale){

        if(scale<0){

            throw new IllegalArgumentException(

            "The scale must be a positive integer or zero");

        }

        BigDecimal b1 = new BigDecimal(v1.toString());

        BigDecimal b2 = new BigDecimal(v2.toString());

        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    /**
     * 返回两个数字之间的所有数字的数组
     * @param beginDate
     * @param endDate
     * @return
     */
    public static String[] getXModesPri(String beginDate , String endDate){
		int year1 = 0;
		int year2 = 0;
		int n = 0;
		String[] strs = null;
		if(endDate != null && !"".equals(endDate) && beginDate != null && !"".equals(beginDate)){
			year1 = DataFormatTool.stringToInteger(beginDate);
			year2 = DataFormatTool.stringToInteger(endDate);
			n = year2-year1+1;
			strs = new String[n];
			for(int i = 0 ; i < n ; i++){
				strs[i] = DataFormatTool.integerToString(year1+i);
			}
		}
		return strs;
	}
    
    /**
     * string转换成unicode
     * @param str
     * @return
     */
    public static String string2Unicode(String str) {  
    	String result = "";  
	    for(int i = 0; i < str.length(); i++) {  
	        String temp = "";  
	        int strInt = str.charAt(i);  
	        if(strInt > 127) {  
	            temp += "u" + Integer.toHexString(strInt)+" ";  
	        } else {  
	            temp = String.valueOf(str.charAt(i));  
	        }  
	          
	        result += temp;  
	    }  
	    return result;  
    }   
      
       
    /**
     * unicode转换成string
     * @param unicodeStr
     * @return
     */
    public static String unicode2String(String unicodeStr){  
    	StringBuffer sb = new StringBuffer();  
        String str[] = unicodeStr.toUpperCase().split("U");  
        for(int i=0;i<str.length;i++){  
          if(str[i].equals("")) continue;  
          String ms = str[i].trim();
          String[] msar = ms.split(" ");
          char c = (char)Integer.parseInt(msar[0].trim(),16);  
    	  sb.append(c);  
          if(msar.length == 2){
        	  sb.append(msar[1]);  
          }
        }  
        return sb.toString();  
    }  

}


