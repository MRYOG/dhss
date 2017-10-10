package com.learninggroup.dhss.cluster;

/**
 * worker节点信息
 * @author BFD_491
 *
 */
public class WorkerInfo {

	
	//该worker节点的主机名
	private String hostName;
	
	//该worker节点的rpc端口
	private String rpcPort;
	
	//该节点上运行的task数量
	private int taskNum;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getRpcPort() {
		return rpcPort;
	}

	public void setRpcPort(String rpcPort) {
		this.rpcPort = rpcPort;
	}

	public int getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(int taskNum) {
		this.taskNum = taskNum;
	}
	
	
	
	
}
