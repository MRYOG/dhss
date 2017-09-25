package com.learninggroup.dhss.core.util;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * <li>功能描述：提供list排序通用�?
 * @author 高俊
 *
 * @param <T>
 * 		要排序的list中的对象的类�?
 */
public class SortListUtil {

	/**
	 * list的排序�?�用方法
	 * @param list
	 * 		要排序的list
	 * @param method
	 * 		属�?�的get方法�?
	 * @param sort
	 * 		排序的类型desc或asc , 默认是desc
	 */
	@SuppressWarnings("unchecked")
	public static <T> void sort(List<T> list , final String method , final String sort){
		Collections.sort(list , new Comparator<T>() {
			public int compare(Object o1 , Object o2){
				int res = 0;
				try {
					Method method1 = ((T)o1).getClass().getMethod(method);
					Method method2 = ((T)o2).getClass().getMethod(method);
					if(sort != null && "desc".equals(sort)){
						res = method2.invoke((T)o2).toString().compareTo(method1.invoke((T)o1).toString());
					}else {
						res = method1.invoke((T)o1).toString().compareTo(method2.invoke((T)o2).toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return res;
			}
		});
	}
	
	/**
	 * 对象列表按date类型排序
	 * @param list
	 * @param method
	 * @param sort
	 */
	@SuppressWarnings("unchecked")
	public static  <T> void sortByDateField(List<T> list , final String method , final String sort){
		Collections.sort(list , new Comparator<T>() {
			public int compare(Object o1 , Object o2){
				int res = 0;
				try {
					Method method1 = ((T)o1).getClass().getMethod(method);
					Method method2 = ((T)o2).getClass().getMethod(method);
					Date date1 = (Date)method2.invoke((T)o2);
					Date date2 = (Date)method1.invoke((T)o1);
					if(sort != null && "desc".equals(sort)){
						
						if(date1 != null && date2 != null){
							res = date1.getTime()>date2.getTime() ? 1 : -1;
						}
					}else {
						
						if(date1 != null && date2 != null){
							res = date2.getTime()>date1.getTime() ? 1 : -1;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return res;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public static  <T> void sortByStringDateField(List<T> list , final String method , final String sort ,final SimpleDateFormat format){
		Collections.sort(list , new Comparator<T>() {
			public int compare(Object o1 , Object o2){
				int res = 0;
				try {
					Method method1 = ((T)o1).getClass().getMethod(method);
					Method method2 = ((T)o2).getClass().getMethod(method);
					Date date1 = DateFormatTool.stringToDate((String)method2.invoke((T)o2), format);
					Date date2 = DateFormatTool.stringToDate((String)method1.invoke((T)o1), format);
					if(sort != null && "desc".equals(sort)){
						
						if(date1 != null && date2 != null){
							res = date1.getTime()>date2.getTime() ? 1 : -1;
						}
					}else {
						
						if(date1 != null && date2 != null){
							res = date2.getTime()>date1.getTime() ? 1 : -1;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return res;
			}
		});
	}
	
	/**
	 * 排序数组列表
	 * @param list
	 * 		要排序的数组列表，和对象列表的排序相比，此处的数组相当于对象
	 * @param index
	 * 		排序字段在数组中的下�?
	 * @param sort
	 * 		排序规则desc,asc
	 */
	@SuppressWarnings("unchecked")
	public static  <T> void sortArray(List<T[]> list , final int index , final String sort){
		Collections.sort(list, new Comparator() {
			public int compare(Object o1 , Object o2){
				int res = 0;
				T[] arr1 = (T[])o1;
				T[] arr2 = (T[])o2;
				if(sort != null && "desc".equals(sort)){
					res = arr1[index].toString().compareTo(arr2[index].toString());
				}else{
					res = arr2[index].toString().compareTo(arr1[index].toString());
				}
				return res;
			}
		});
	}
	
	
	
	public static void main(String[] args) {
		List<String[]> list = new ArrayList<String[]>();
		String[] a1 = {"在是","22","三�?�修改版"};
		String[] a2 = {"在d�?","22","二�?�上传版"};
		String[] a3 = {"在f�?","21","二�?�上传版"};
		String[] a4 = {"aa�?","21","三�?�修改版"};
		String[] a5 = {"f�?","23","�?、预审版"};
		String[] a6 = {"在s","2f","�?、预审版"};
		list.add(a1);
		list.add(a2);
		list.add(a3);
		list.add(a4);
		list.add(a5);
		list.add(a6);
		SortListUtil.sortArray(list, 2, null);
		for(String[] mm : list){
			System.out.println(mm[2]);
		}
	}
}
