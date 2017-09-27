package com.learninggroup.dhss.zk;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;

import com.learninggroup.dhss.core.conf.DHSSConfiguration;
import com.learninggroup.dhss.core.util.PropertiesTools;

/**
 * dhss-master节点使用的单例的zkclient
 * @author BFD_491
 *
 */
public class DMasterCuratorExcutor extends AbstractZKClient {
	
	private String zNodeMasterHaPath = null;
	
	private String myZNode = null;
	
	private PathChildrenCache pc = null;
	
	private PathChildrenCacheListener pclist = null;
	
	private  boolean isActive = false;
	
	private static DMasterCuratorExcutor dmCuratorExcutor = null;
	
	
	private DMasterCuratorExcutor(){
		super();
		this.initSystemZNode();
		this.listenerMaster();
		this.registMaster();
	}

	@Override
	public void doAfterReConnect() {
		this.initSystemZNode();
		this.listenerMaster();
		this.registMaster();

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
	 * 初始化系统znode
	 */
	public void initSystemZNode(){
		try {
			zNodeMasterHaPath = PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_ZNODE_PARENT) + PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_ZNODE_HA);

			//如果在其它master节点创建完成/dhss节点之后出现异常，导致/dhss/dhss-ha节点未创建成功，这里多一个判断
			if(zkClient.checkExists().forPath(zNodeMasterHaPath) == null){
				zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(zNodeMasterHaPath);
				log.info("完成添加节点:"+zNodeMasterHaPath); 
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
		pc = new PathChildrenCache(zkClient, zNodeMasterHaPath, true); 
		try {
			pc.start();
			pclist = new PathChildrenCacheListener() {
				
				public void childEvent(CuratorFramework zkClient1, PathChildrenCacheEvent event)
						throws Exception {
		            
					List<ChildData> listMaster = pc.getCurrentData();
					if(listMaster != null && listMaster.size() > 0){
						ChildData minDn = listMaster.get(0);
						if(myZNode.equals(minDn.getPath())){
							isActive = true;
							//上线处理
							log.info("active节点是："+myZNode);
						}else{
							isActive = false;
							//下线处理
							log.info("节点："+myZNode+" 为standby节点");
						}
					}
				}
			};
			pc.getListenable().addListener(pclist);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getzNodeMasterHaPath() {
		return zNodeMasterHaPath;
	}

	public void setzNodeMasterHaPath(String zNodeMasterHaPath) {
		this.zNodeMasterHaPath = zNodeMasterHaPath;
	}

	

	public PathChildrenCache getPc() {
		return pc;
	}

	public void setPc(PathChildrenCache pc) {
		this.pc = pc;
	}

	public PathChildrenCacheListener getPclist() {
		return pclist;
	}

	public void setPclist(PathChildrenCacheListener pclist) {
		this.pclist = pclist;
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
