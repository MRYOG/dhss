package com.learninggroup.dhss.cluster;

import org.apache.curator.framework.CuratorFramework;
import org.apache.log4j.Logger;

import com.learninggroup.dhss.core.conf.DHSSConfiguration;
import com.learninggroup.dhss.core.util.PropertiesTools;
import com.learninggroup.dhss.zk.ZKClientSingleton;



/**
 * <li>master节点，主要功能包括：
 * <li>向zk注册自己，和其它的master节点争抢active角色
 * @author BFD_491
 *
 */
public class DMaster {
	
	public static final Logger log = Logger.getLogger(DMaster.class);
	
	public CuratorFramework zkClient = null;
	
	private String zNodeParentPath = null;
	
	private String zNodeMasterHaPath = null;
	
	public static void main(String[] args) {
		DMaster dm = new DMaster();
		dm.zkClient = ZKClientSingleton.newClient();
		ZKClientSingleton.start();
		dm.initSystemZNode();
		ZKClientSingleton.close();
				
	}
	
	/**
	 * 初始化系统znode
	 */
	public void initSystemZNode(){
		try {
			
			zNodeParentPath = PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_ZNODE_PARENT);
			zNodeMasterHaPath = PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_ZNODE_PARENT) + PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_ZNODE_HA);
			if(zkClient.checkExists().forPath(zNodeParentPath) == null){
		        System.out.println("添加成功！！！");  
				zkClient.create().forPath(zNodeParentPath);
				zkClient.create().forPath(zNodeMasterHaPath);
			}
			//如果在其它master节点创建完成/dhss节点之后出现异常，导致/dhss/dhss-ha节点未创建成功，这里多一个判断
			if(zkClient.checkExists().forPath(zNodeMasterHaPath) == null){
				zkClient.create().forPath(zNodeMasterHaPath);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("创建zk节点失败！");
		}
	}

	public CuratorFramework getZkClient() {
		return zkClient;
	}

	public void setZkClient(CuratorFramework zkClient) {
		this.zkClient = zkClient;
	}
	
	
	
	
}
