package com.learninggroup.dhss.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;

import com.learninggroup.dhss.cluster.DMaster;
import com.learninggroup.dhss.core.config.DHSSConfiguration;
import com.learninggroup.dhss.core.util.DataFormatTool;
import com.learninggroup.dhss.core.util.PropertiesTools;

public abstract class AbstractZKClient {

	public static final Logger log = Logger.getLogger(DMaster.class);
	
	protected static CuratorFramework zkClient = null;
	
	
	//watcher列表，当zk连接状态Expired后，重新连接zk并注意watcher
	
	public AbstractZKClient() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_QUORUM , "localhost"));
		
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(
				DataFormatTool.stringToInteger(PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_RETRY_SLEEP , "1000")), 
				DataFormatTool.stringToInteger(PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_RETRY_MAXTIME , "5")));
        
		
		log.info("zk地址："+sb.toString());
		
		try{
			//创建zkclient
			zkClient = CuratorFrameworkFactory.builder()
						.connectString(sb.toString())
						.retryPolicy(retryPolicy)
						.sessionTimeoutMs(1000 * DataFormatTool.stringToInteger(PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_SESSION_TIMEOUT , "6")))
						.connectionTimeoutMs(1000 * DataFormatTool.stringToInteger(PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_SESSION_TIMEOUT , "6")))
						.build();
			
			zkClient.start();
			initStateLister();
			
		}catch(Exception e){
			e.printStackTrace();
			log.error("创建zk连接失败");
		}
    }
	
	/**
	 * 给zkclient注册state监听事件
	 */
	public void initStateLister(){
		if(zkClient == null)return;
		//添加ConnectionStateListener监控zk连接状态
		ConnectionStateListener csLister = new ConnectionStateListener() {
			
			public void stateChanged(CuratorFramework client, ConnectionState newState) {
				log.info("state changed…" + newState.name());
				if(newState == ConnectionState.LOST){
					//由各个子类实现，当zk连接lost时该做的事
					log.info("zk connection is lost , run the doAtConnectLost method ...");
					doAtConnectLost();
					int i = 0;
					while(true){
						log.info("trying to re-connect to zk..." + (i++));
						try {
							//手动重连，直到连接成功
							if(client.getZookeeperClient().blockUntilConnectedOrTimedOut()){
								log.info("trying to do after reconnect success...");
								//由各个子类定义其需要执行的操作
								doAfterReConnect();
								//因为zk重新连接，所以需要重新注册state监控事件
								initStateLister();
								break;
							}
						}catch(InterruptedException e){
							break;
						}catch (Exception e) {
							
						}
					}
				}
			}
		};
		
		zkClient.getConnectionStateListenable().addListener(csLister);
	}
	
	//zk连接lost后需要做的操作
	public abstract void doAtConnectLost();

	//重新连接zk后需要做的操作
	public abstract void doAfterReConnect();

    public static void start() {
    	log.info("打开zk连接");
    	zkClient.start();
    }

    public static void close() {
    	log.info("关闭zk连接");
    	zkClient.close();
    }

	public static CuratorFramework getZkClient() {
		return zkClient;
	}

	public static void setZkClient(CuratorFramework zkClient) {
		AbstractZKClient.zkClient = zkClient;
	}
    
    
}
