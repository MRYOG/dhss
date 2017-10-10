package com.learninggroup.dhss.cluster;

import java.util.HashMap;
import java.util.List;

/**
 * 集群信息
 * @author BFD_491
 *
 */
public class ClusterInfo {

	public static String activeMaster;
	
	public static List<String> standbyMaster;
	
	public static HashMap<String, WorkerInfo> workerMap = new HashMap<String, WorkerInfo>();

	
}
