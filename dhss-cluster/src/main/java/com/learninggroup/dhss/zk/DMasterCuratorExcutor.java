package com.learninggroup.dhss.zk;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;

import com.learninggroup.dhss.core.conf.DHSSConfiguration;
import com.learninggroup.dhss.core.util.PropertiesTools;

/**
 * dhss-master节点使用的单例的zkclient
 * @author BFD_491
 *
 */
public class DMasterCuratorExcutor extends AbstractZKClient {
	
	public static final Logger log = Logger.getLogger(DMasterCuratorExcutor.class);
	
	private String zNodeMasterHaPath = null;
	
	private String zNodeWorkerPath = null;
	
	private String myZNode = null;
	
	private PathChildrenCache masterPc = null;
	
	private PathChildrenCache workerPc = null;
	
	private MasterPcListener masterPcListener = null;
	
	private WorkerPcListener workerPcListener = null;
	
	private  boolean isActive = false;
	
	private static DMasterCuratorExcutor dmCuratorExcutor = new DMasterCuratorExcutor();
	
	
	private DMasterCuratorExcutor(){
		super();
		init();
	}

	@Override
	public void doAfterReConnect() {
		init();
	}
	
	/**
	 * 初始化
	 */
	public void init(){
		this.initSystemZNode();
		this.listenerMaster();
		this.registMaster();
		this.listenerWorker();
	}

	@Override
	public void doAtConnectLost() {
		this.isActive = false;
	}

	public static synchronized DMasterCuratorExcutor getCuratorExcutor(){
		if(dmCuratorExcutor == null){
			dmCuratorExcutor = new DMasterCuratorExcutor();
		}
		
		return dmCuratorExcutor;
	}
	
	/**
	 * 初始化系统master znode
	 */
	public void initSystemZNode(){
		try {
			zNodeMasterHaPath = PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_ZNODE_PARENT) + PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_ZNODE_HA);
			zNodeWorkerPath = PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_ZNODE_PARENT) + PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_ZNODE_WORKER);
			
			if(zkClient.checkExists().forPath(zNodeMasterHaPath) == null){
				zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(zNodeMasterHaPath);
				log.info("完成添加节点:"+zNodeMasterHaPath); 
			}
			
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
	 * 注册master节点
	 */
	public void registMaster(){
		try {
			myZNode = zNodeMasterHaPath + "/master_";
			myZNode = zkClient.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(myZNode, "".getBytes());
			log.info("注册master节点成功："+myZNode);
		} catch (Exception e) {
			log.info("注册master节点失败："+myZNode);
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加对/dhss/dhss-ha节点的监控，如果有master节点上线或下线，需要判断是否变更active master节点
	 */
	public void listenerMaster(){
		masterPc = new PathChildrenCache(zkClient, zNodeMasterHaPath, true); 
		try {
			masterPc.start();
			masterPcListener = new MasterPcListener();
			masterPcListener.setDmCuratorExcutor(dmCuratorExcutor);
			masterPcListener.setMasterPc(masterPc);
			masterPc.getListenable().addListener(masterPcListener);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 添加对/dhss/dhss-worker节点的监控，如果有worker节点上线或下线，需要做相关的处理
	 */
	public void listenerWorker(){
		workerPc = new PathChildrenCache(zkClient, zNodeWorkerPath, true); 
		try {
			workerPc.start();
			workerPcListener = new WorkerPcListener();
			workerPcListener.setWorkerPc(workerPc);
			workerPcListener.setDmCuratorExcutor(dmCuratorExcutor);
			workerPc.getListenable().addListener(workerPcListener);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static DMasterCuratorExcutor getDmCuratorExcutor() {
		return dmCuratorExcutor;
	}

	public static void setDmCuratorExcutor(DMasterCuratorExcutor dmCuratorExcutor) {
		DMasterCuratorExcutor.dmCuratorExcutor = dmCuratorExcutor;
	}

	public String getMyZNode() {
		return myZNode;
	}

	public void setMyZNode(String myZNode) {
		this.myZNode = myZNode;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}



	
}
