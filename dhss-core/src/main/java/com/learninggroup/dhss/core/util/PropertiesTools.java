package com.learninggroup.dhss.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.learninggroup.dhss.core.conf.DHSSConfiguration;

/**
 * <li>功能描述：资源加载类
 * @author 高俊
 *
 */
public class PropertiesTools {
	public static final Logger log = Logger.getLogger(PropertiesTools.class);
	public static Properties properties = null;
	public static File proFile = null;
	
	static{
		properties = ClassLoaderUtil.getProperties(DHSSConfiguration.DHSS_SITE_FILEPATH);
	}
	
	
	/**
	 * 获取配置项的值
	 * @param key
	 * @return
	 */
	public static String getValue(String key){
		if(properties == null){
			return null;
		}
		return properties.getProperty(key);
	}
	
	/**
	 * 获取配置值,有默认值 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getValue(String key , String defaultValue){
		if(properties == null){
			return null;
		}
		return properties.getProperty(key , defaultValue);
	}
	
	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		sb.append(PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_QUORUM , "localhost"))
			.append(PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_ZNODE_PARENT , "/dhss"))
			.append(PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_ZNODE_HA , "/dhss-ha"));
		System.out.println(sb.toString());
	}
	

}
