package com.learninggroup.dhss.zk;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.log4j.Logger;

public class MasterPcListener implements PathChildrenCacheListener {
	
	public static final Logger log = Logger.getLogger(MasterPcListener.class);
	
	private PathChildrenCache masterPc = null;
	
	private  DMasterCuratorExcutor dmCuratorExcutor = null;

	public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
			throws Exception {
		
		List<ChildData> listMaster = masterPc.getCurrentData();
		if(listMaster != null && listMaster.size() > 0){
			ChildData minDn = listMaster.get(0);
			if(dmCuratorExcutor.getMyZNode().equals(minDn.getPath())){
				dmCuratorExcutor.setActive(true);
				//上线处理
				log.info("active节点是："+dmCuratorExcutor.getMyZNode());
			}else{
				dmCuratorExcutor.setActive(false);
				//下线处理
				log.info("节点："+dmCuratorExcutor.getMyZNode()+" 为standby节点");
			}
		}

	}

	public PathChildrenCache getMasterPc() {
		return masterPc;
	}

	public void setMasterPc(PathChildrenCache masterPc) {
		this.masterPc = masterPc;
	}

	public DMasterCuratorExcutor getDmCuratorExcutor() {
		return dmCuratorExcutor;
	}

	public void setDmCuratorExcutor(DMasterCuratorExcutor dmCuratorExcutor) {
		this.dmCuratorExcutor = dmCuratorExcutor;
	}
	
	

}
