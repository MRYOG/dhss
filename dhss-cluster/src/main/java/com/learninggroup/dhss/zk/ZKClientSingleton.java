package com.learninggroup.dhss.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;

import com.learninggroup.dhss.cluster.DMaster;
import com.learninggroup.dhss.core.conf.DHSSConfiguration;
import com.learninggroup.dhss.core.util.DataFormatTool;
import com.learninggroup.dhss.core.util.PropertiesTools;

public class ZKClientSingleton {

	public static final Logger log = Logger.getLogger(DMaster.class);
	
	private static CuratorFramework zkClient = null;
	
	private ZKClientSingleton() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_QUORUM , "localhost"))
			.append(PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_ZNODE_PARENT , "/dhss"));
		
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(
				DataFormatTool.stringToInteger(PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_RETRY_SLEEP , "1000")), 
				DataFormatTool.stringToInteger(PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_RETRY_MAXTIME , "5")));
        
		
		log.info("zk地址："+sb.toString());
		
		try{
			
			zkClient = CuratorFrameworkFactory.builder()
					.connectString(sb.toString())
					.retryPolicy(retryPolicy)
					.sessionTimeoutMs(1000 * DataFormatTool.stringToInteger(PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_SESSION_TIMEOUT , "6")))
					.connectionTimeoutMs(1000 * DataFormatTool.stringToInteger(PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_SESSION_TIMEOUT , "6")))
					.build();
		}catch(Exception e){
			e.printStackTrace();
			log.error("创建zk连接失败");
		}
    }

    public static synchronized CuratorFramework newClient() {
        if (zkClient == null) {
            new ZKClientSingleton();
        }
        return zkClient;
    }

    public static void start() {
    	log.info("打开zk连接");
    	zkClient.start();
    }

    public static void close() {
    	log.info("关闭zk连接");
    	zkClient.close();
    }
}
