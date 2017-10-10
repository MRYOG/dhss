package com.learninggroup.dhss.zk;

import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;

import com.learninggroup.dhss.core.conf.DHSSConfiguration;
import com.learninggroup.dhss.core.util.PropertiesTools;

/**
 * dhss-worker节点使用的单例的zkclient
 * @author BFD_491
 *
 */
public class DWorkerCuratorExcutor extends AbstractZKClient {
	
	public static final Logger log = Logger.getLogger(DWorkerCuratorExcutor.class);
	
	private String zNodeWorkerPath = null;
	
	private String zNodeTaskPath = null;
	
	private String myZNode = null;
	
	private PathChildrenCache pc = null;
	
	private PathChildrenCacheListener pclist = null;
	
	private static DWorkerCuratorExcutor dwCuratorExcutor = null;
	
	private String taskNode = null;
	
	
	private DWorkerCuratorExcutor(){
		super();
		this.initSystemZNode();
		this.registWorker();
	}

	@Override
	public void doAfterReConnect() {
		this.initSystemZNode();
		this.registWorker();

	}

	@Override
	public void doAtConnectLost() {
		
	}

	public static synchronized DWorkerCuratorExcutor getCuratorExcutor(){
		if(dwCuratorExcutor == null){
			dwCuratorExcutor = new DWorkerCuratorExcutor();
		}
		
		return dwCuratorExcutor;
	}
	
	/**
	 * 初始化系统worker znode
	 */
	public void initSystemZNode(){
		try {
			zNodeWorkerPath = PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_ZNODE_PARENT) + PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_ZNODE_WORKER);
			 
			if(zkClient.checkExists().forPath(zNodeWorkerPath) == null){
				zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(zNodeWorkerPath);
				log.info("完成添加节点:"+zNodeWorkerPath); 
			}


		} catch (Exception e) {
			e.printStackTrace();
			log.error("创建zk节点失败！");
		}
	}
	
	/**
	 * 注册worker节点
	 */
	public void registWorker(){
		try {
			myZNode = zNodeWorkerPath + "/worker_";
			myZNode = zkClient.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(myZNode, "".getBytes());
			log.info("注册worker节点成功："+myZNode);
			log.info("创建task存储节点...");
			String workerNode = myZNode.substring(myZNode.lastIndexOf("/")+1);
			zNodeTaskPath = PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_ZNODE_PARENT) + PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_ZNODE_TASK) + "/" + workerNode;
			zNodeTaskPath = zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(zNodeTaskPath, "".getBytes());
			log.info("创建task存储节点成功："+zNodeTaskPath);
		} catch (Exception e) {
			log.info("注册worker节点失败："+myZNode);
			e.printStackTrace();
		}
	}
	
	
	public void updateMyZNodeData(String data){
		try {
			zkClient.setData().forPath(myZNode,data.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getMyZNode() {
		return myZNode;
	}

	public void setMyZNode(String myZNode) {
		this.myZNode = myZNode;
	}


	
}
