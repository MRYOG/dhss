package com.learninggroup.dhss.cluster;

import org.apache.curator.framework.CuratorFramework;
import org.apache.log4j.Logger;

import com.learninggroup.dhss.zk.DMasterCuratorExcutor;



/**
 * <li>master节点，主要功能包括：
 * <li>向zk注册自己，和其它的master节点争抢active角色
 * @author BFD_491
 *
 */
public class DMaster {
	
	public static final Logger log = Logger.getLogger(DMaster.class);
	
	public CuratorFramework zkClient = null;
	
	private static DMasterCuratorExcutor dmCuratorExcutor = null;
	
	
	public static void main(String[] args) {
		DMaster dm = new DMaster();
//		dmCuratorExcutor = DMasterCuratorExcutor.getCuratorExcutor();
		
		try {
			while (true) {
//				if(dmCuratorExcutor.isActive()){
//					Thread.sleep(3000);
//					System.out.println(dmCuratorExcutor.getMyZNode() + " is active node");
//				}
				Thread.sleep(3000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
				
	}

	
	
	
}
