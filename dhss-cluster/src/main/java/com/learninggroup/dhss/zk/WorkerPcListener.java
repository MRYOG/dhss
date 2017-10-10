package com.learninggroup.dhss.zk;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.log4j.Logger;

import com.learninggroup.dhss.cluster.ClusterInfo;
import com.learninggroup.dhss.cluster.WorkerInfo;
import com.learninggroup.dhss.core.conf.DHSSConfiguration;
import com.learninggroup.dhss.core.util.DataFormatTool;
import com.learninggroup.dhss.core.util.PropertiesTools;

/**
 * worker节点的监听器
 * @author BFD_491
 *
 */
public class WorkerPcListener implements PathChildrenCacheListener {
	
	public static final Logger log = Logger.getLogger(WorkerPcListener.class);
	
	private PathChildrenCache workerPc = null;
	
	private  DMasterCuratorExcutor dmCuratorExcutor = null;

	public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
			throws Exception {
		
		List<ChildData> listWorker = workerPc.getCurrentData();
		if(listWorker == null || listWorker.size() == 0){
			ClusterInfo.workerMap.clear();
		}
		
		String nodePath = event.getData().getPath();
		String nodeHost = nodePath.substring(nodePath.lastIndexOf("/")+1);
        
        if(Type.CHILD_REMOVED.equals(event.getType())){
        	if(dmCuratorExcutor.isActive()){
        		log.info("负载节点["+nodeHost+"]上的tasks...");
        		String taskPath = this.doAfterWorkerRemove(nodeHost);
        		log.info("已将节点["+nodeHost+"]上的tasks状态设置为 failed ! 删除zk上的["+taskPath+"]节点");
//        		dmCuratorExcutor.zkClient.
        	}
        	
        }else if(Type.CHILD_UPDATED.equals(event.getType())){
        	
        	String data = new String(event.getData().getData(),"UTF-8");
        	log.info("节点["+nodeHost+"]数据变更！"+data);
        	if(ClusterInfo.workerMap.get(nodeHost) != null){
        		WorkerInfo wi = ClusterInfo.workerMap.get(nodeHost);
        		wi.setTaskNum(DataFormatTool.stringToInteger(data.split(":")[1]));
        	}else{
        		WorkerInfo wi = new WorkerInfo();
        		wi.setHostName(nodeHost);
        		wi.setTaskNum(DataFormatTool.stringToInteger(data.split(":")[1]));
        		ClusterInfo.workerMap.put(nodeHost, wi);
        	}
        }else if(Type.CHILD_ADDED.equals(event.getType())){
        	log.info("节点["+nodeHost+"]上线！");
        	WorkerInfo wi = new WorkerInfo();
    		wi.setHostName(nodeHost);
    		wi.setTaskNum(0);
    		ClusterInfo.workerMap.put(nodeHost, wi);
        }

	}
	
	/**
	 * 如果有worker节点挂掉，需要处理挂掉节点上的task
	 * @param nodeHost
	 */
	public String doAfterWorkerRemove(String nodeHost){
		StringBuilder sb = new StringBuilder();
		sb.append(PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_ZNODE_PARENT))
			.append(PropertiesTools.getValue(DHSSConfiguration.DHSS_ZOOKEEPER_ZNODE_TASK))
			.append("/")
			.append(nodeHost);
		
		String taskPath = sb.toString();
		//找到该目录下所有的节点
		System.out.println("负载"+taskPath);
		
		return taskPath;
		
	}

	public PathChildrenCache getWorkerPc() {
		return workerPc;
	}

	public void setWorkerPc(PathChildrenCache workerPc) {
		this.workerPc = workerPc;
	}

	public DMasterCuratorExcutor getDmCuratorExcutor() {
		return dmCuratorExcutor;
	}

	public void setDmCuratorExcutor(DMasterCuratorExcutor dmCuratorExcutor) {
		this.dmCuratorExcutor = dmCuratorExcutor;
	}

	
	
}
