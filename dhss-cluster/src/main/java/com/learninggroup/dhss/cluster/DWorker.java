package com.learninggroup.dhss.cluster;

import java.util.Random;

import org.apache.curator.framework.CuratorFramework;
import org.apache.log4j.Logger;

import com.learninggroup.dhss.zk.DWorkerCuratorExcutor;


public class DWorker {
	public static final Logger log = Logger.getLogger(DMaster.class);
	
	public CuratorFramework zkClient = null;
	
	private static DWorkerCuratorExcutor dwCuratorExcutor = null;
	
	
	
	
	public static void main(String[] args) {
		DWorker dw = new DWorker();
		dwCuratorExcutor = DWorkerCuratorExcutor.getCuratorExcutor();
		Random random = new Random();
		int i = 0;
		try {
			while (true) {
//				System.out.println(dwCuratorExcutor.getMyZNode() + " is working...");
				int max = 20;
				int min = 10;
				int mm = random.nextInt(max - min + 1)+min;
				i++;
				dwCuratorExcutor.updateMyZNodeData("taskNum:"+i);
				Thread.sleep(20000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
				
	}

	
}
