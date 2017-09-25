package com.learninggroup.dhss.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * <li>功能描述：用于操作文件资源的工具�?
 * @author 高俊
 *
 */
public class FileTool {
	public static Logger logger = Logger.getLogger(FileTool.class);
	
	/** windows操作系统 */
	public static final int SYS_WIN = 1;
	
	/** Linux操作系统 */
	public static final int SYS_LINUX = 2;
	
	/** 无法识别的操作系�? */
	public static final int SYS_UNKNOW = -1;
	
	private static final int BUFFER_SIZE = 16 * 1024;
	

	/**
	 * 解压rar文件  带密码返回true 否则false
	 * 
	 * @param targetPath
	 * @param absolutePath
	 * @return 是否带密码，不带密码为excel数据，带密码是数据库数据�?
	 */
	@SuppressWarnings("unused")
	public static void unRarFile(String targetPath, String absolutePath) { 
		System.out.println("�?始解压文件�??"+absolutePath+"�? �? �?"+targetPath+"�?");
		ResourceBundle rb = ResourceBundle.getBundle("constraint");
		//判断本地系统类型
		String os = System.getProperty("os.name").toLowerCase();
		//解压缩密�?
		String password=rb.getString("PARSE_PASSWORD");
		boolean isPassword = false;
		//windows 系统
		if (os.indexOf("win") >= 0) {
			try {
				
				String cmd = rb.getString("winrar_execpath");
				// 系统安装winrar的路�?
				// String cmd = "C:\\Program Files\\WinRAR\\winrar.exe";
				// 无解压密码命�?
				String unrarCmd = cmd + " x -r -p- -o+ " + absolutePath + " "
						+ targetPath;
				//有解压密码命�?
				String unrarCmdByPassword = cmd + " x -r -p"+password+" -o+ " + absolutePath + " "
				+ targetPath;
				
				Runtime rt = Runtime.getRuntime();
				Process pre = rt.exec(unrarCmd);
				long timeBefor = System.currentTimeMillis();
				//等待4�?  之后判断解压缩是否完成�??
				Thread.currentThread().sleep(4000);
				try{
					pre.exitValue();
				}catch(IllegalThreadStateException e){
					isPassword = true;
					pre.destroy();
					Process preByPassword = rt.exec(unrarCmdByPassword);
					System.out.println(preByPassword.waitFor());
				}
				System.out.println(pre.waitFor());
				pre.destroy();

			} catch (Exception e) {
				System.out.println("解压发生异常");
				return;
			}
		} else {

			try {
				String unrarCmd = "/usr/local/bin/rar x -inul  -o+ " + absolutePath + " "
						+ targetPath;
				//有解压密码命�?
				String unrarCmdByPassword = "/usr/local/bin/rar x -inul -p"+password+" -o+ "  + absolutePath + " "
				+ targetPath;
				Runtime rt = Runtime.getRuntime();
				System.out.println("unrarCmd======:"+unrarCmd);
				Process pre = rt.exec(unrarCmd);
				System.out.println("unrarCmd======:"+unrarCmd);
				long timeBefor = System.currentTimeMillis();
				//等待4�?  之后判断解压缩是否完成�??
				Thread.currentThread().sleep(4000);
				try{
					//System.out.println(pre.exitValue());
					if(pre.exitValue()!=0){
						isPassword = true;
						pre.destroy();
						System.out.println("unrarCmdByPassword======:"+unrarCmdByPassword);
						Process preByPassword = rt.exec(unrarCmdByPassword);
						System.out.println("unrarCmdByPassword======:"+unrarCmdByPassword);
						System.out.println(preByPassword.waitFor());
					}
				}catch(IllegalThreadStateException e){
					isPassword = true;
					pre.destroy();
					System.out.println("unrarCmdByPassword======:"+unrarCmdByPassword);
					Process preByPassword = rt.exec(unrarCmdByPassword);
					System.out.println("unrarCmdByPassword======:"+unrarCmdByPassword);
					System.out.println(preByPassword.waitFor());
				}
				System.out.println(pre.waitFor());
				pre.destroy();

			} catch (Exception e) {
				System.out.println("解压发生异常");
				e.printStackTrace();
				return;
			}
		}
		System.out.println("解压完成...");
	}
	
	/**
	 * 解压zip格式的压缩文件到指定位置
	 * 
	 * @param zipFileName
	 *            压缩文件
	 * @param extPlace
	 *            解压目录
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static synchronized void unzip(String zipFileName, String extPlace)
			throws Exception {
		System.out.println("�?始解压文件�??"+zipFileName+"�? �? �?"+extPlace+"�?");
		try {
			(new File(extPlace)).mkdirs();
			File f = new File(zipFileName);
			ZipFile zipFile = new ZipFile(zipFileName);
			if ((!f.exists()) && (f.length() <= 0)) {
				throw new Exception("要解压的文件不存�?!");
			} else {
				f = null;
			}
			String strPath, gbkPath, strtemp;
			File tempFile = new File(extPlace);
			strPath = tempFile.getAbsolutePath();
			//把所有的'/'�?'\'都替换成系统文件做分隔符
			strPath = strPath.replace('/', File.separatorChar);
			strPath = strPath.replace('\\', File.separatorChar);
			java.util.Enumeration e = zipFile.getEntries();
			while (e.hasMoreElements()) {
				
				org.apache.tools.zip.ZipEntry zipEnt = (ZipEntry) e
						.nextElement();
				gbkPath = zipEnt.getName();
				//把所有的'/'�?'\'都替换成系统文件做分隔符
				gbkPath = gbkPath.replace('/', File.separatorChar);
				gbkPath = gbkPath.replace('\\', File.separatorChar);
				if (zipEnt.isDirectory()) {
					strtemp = strPath + File.separator + gbkPath;
					File dir = new File(strtemp);
					dir.mkdirs();
					continue;
				} else {
					// 读写文件
					InputStream is = zipFile.getInputStream(zipEnt);
					BufferedInputStream bis = new BufferedInputStream(is);
					strtemp = strPath + File.separator + gbkPath;
					
					// 建目�?
					String strsubdir = gbkPath;
					for (int i = 0; i < strsubdir.length(); i++) {
						if (strsubdir.substring(i, i + 1).equalsIgnoreCase(
								File.separator)) {
							String temp = strPath + File.separator
									+ strsubdir.substring(0, i);
							File subdir = new File(temp);
							if (!subdir.exists())
								subdir.mkdir();
						}
					}
					FileOutputStream fos = new FileOutputStream(strtemp);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					int c;
					while ((c = bis.read()) != -1) {
						bos.write((byte) c);
					}
					bos.close();
					fos.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.out.println("解压完成...");
	}
	
	/**
	 * 遍历目录找出�?有指定后�?名的文件
	 * @param dir
	 * @return
	 */
	public static String[] serachFiles(String dir , String endName) {
		StringBuffer temp = new StringBuffer();
        File root = new File(dir);
        File[] filesOrDirs = root.listFiles();

        for (int i = 0; i < filesOrDirs.length; i++) {
            if (filesOrDirs[i].isDirectory()) {
                serachFiles(filesOrDirs[i].getAbsolutePath() , endName);
            } else if(filesOrDirs[i].toString().toUpperCase().endsWith(endName)){
            	if(temp==null){
            		temp = new StringBuffer();
            	}
                temp.append(filesOrDirs[i].getAbsoluteFile()).append(",");
            }
        }

        return temp.toString().split(",");

    }
	
	/**
	 * 获取系统类型
	 * @return
	 */
	public static int getSystemType(){
		String os = System.getProperty("os.name").toLowerCase();
		if(os.indexOf("win") >= 0){
			return SYS_WIN;
		}else{
			return SYS_LINUX;
		}
	}
	
	/**
	 * 复制�?个文件到指定目录
	 * @param sourceFile
	 * 		源文件决定路�?
	 * @param targetPath
	 * 		目标目录
	 * @return
	 * @throws Exception
	 */
	public static String copyFile(String sourceFile , String targetFile){
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			if(sourceFile == null || "".equals(sourceFile) || targetFile == null || "".equals(targetFile)){
				return null;
			}
			sourceFile = sourceFile.replace("/", File.separator).replace("\\", File.separator);
			targetFile = targetFile.replace("/", File.separator).replace("\\", File.separator);
			
			File sourceFile_e = new File(sourceFile);
			if(!sourceFile_e.exists()){
				System.out.println("文件[ "+sourceFile +" ]"+"不存�?");
				return null;
			}
			
			File targetPathFile = new File(targetFile);
			if(!targetPathFile.exists() || !targetPathFile.isFile()){
				String dir = targetFile.substring(0,targetFile.lastIndexOf(File.separator));
				File dirFile = new File(dir);
				if(!dirFile.exists() || !dirFile.isDirectory()){
					dirFile.mkdirs();
				}
					
				targetPathFile.createNewFile();
			}
			
		
			in = new BufferedInputStream(new FileInputStream(sourceFile_e));
			out = new BufferedOutputStream(new FileOutputStream(targetPathFile));
			int len = 1024;
			int relen = -1;
			byte[] b = new byte[len];
			while((relen = in.read(b,0,len)) != -1){
				out.write(b,0,relen);
			}
		} catch (Exception e) {
			targetFile = null;
			e.printStackTrace();
		}finally{
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return targetFile;
	}
	

	
	/**
	 * <li>功能描述：删除目录下的所有指定文件，如果删除完成后目录为空，则目录也�?并删�?
	 * @param filePath
	 * 			目录或文件路�?
	 * @param filterFileNames
	 * 			�?要删除的文件路径
	 * @param exceptionFileNames
	 * 			如果为空则也要删除的目录路径
	 * @return
	 */
	public static boolean deleteFile(String filePath , List<String> filterFileNames){
		File file  = new File(filePath);
		if(file.exists()){
			
			if(file.isDirectory()){
				deleteFiles(file , filterFileNames);
			}
			if(filterFileNames.contains(filePath)){
				file.delete();
				logger.info("删除文件�?"+file.getAbsolutePath()+"�?");
			}
			
		}else{
			logger.info("文件�?"+filePath+"】不存在");
			return false;
		}
		return true;
	}
	
	private static boolean deleteFiles(File file ,List<String> filterFileNames){
		if(file == null){
			logger.info("文件为null");
			return true;
		}
		try{
			File[] files = file.listFiles();
			for(int i = 0 ; i < files.length ; i++){
				File thisFile = files[i];
				if(thisFile.isDirectory()){
					deleteFiles(thisFile , filterFileNames);
				}
				if(filterFileNames.contains(thisFile.getAbsolutePath())){
					thisFile.delete();
					logger.info("删除文件�?"+thisFile.getAbsolutePath()+"�?");
				}
			}
			if(filterFileNames.contains(file.getAbsolutePath())){
				file.delete();
				logger.info("删除文件�?"+file.getAbsolutePath()+"�?");
			}
			
		}catch (Exception e) {
			logger.error("删除文件�?"+file.getPath()+"】失�?");
			return false;
		}
		return true;
		
	}
	
	/**
	 * 清空目录
	 * @param filePath
	 * 		目录�?
	 * @return
	 * @author 高俊
	 */
	public static boolean deleteFile(String filePath){
		File file  = new File(filePath);
		if(file.exists()){
			if(file.isDirectory()){
				deleteFiles(file);
			}
			file.delete();
			
		}else{
			logger.info("文件�?"+filePath+"】不存在");
			return false;
		}
		return true;
	}
	
	private static boolean deleteFiles(File file){
		if(file == null){
			logger.info("文件为null");
			return true;
		}
		try{
			File[] files = file.listFiles();
			for(int i = 0 ; i < files.length ; i++){
				File thisFile = files[i];
				if(thisFile.isDirectory()){
					deleteFiles(thisFile);
				}
				thisFile.delete();
			}
			file.delete();
			
		}catch (Exception e) {
			logger.error("删除文件�?"+file.getPath()+"】失�?");
			return false;
		}
		return true;
	}
	
	/**
	 * 压缩文件到rar
	 * @param targetFile
	 * @param fileName
	 * @param isPass
	 * @return
	 */
	public static boolean toRAR(String targetFile , String fileName , Boolean isPass){
		if(targetFile == null || fileName == null || isPass == null)
			return false;
		
		
		System.out.println("压缩文件["+fileName+"]到["+targetFile+"]");
		
		boolean isSuccess = true;
		
		ResourceBundle rb = ResourceBundle.getBundle("constraint");
		//本地系统类型
		String os = System.getProperty("os.name").toLowerCase();
		//解压缩密�?
		String password=rb.getString("PARSE_PASSWORD");
		//是否�?要解压密�?
		if(os.contains("win")){//windows操作系统
			String cmd = rb.getString("winrar_execpath");
			if(isPass){
				cmd = cmd+" a -p"+password+" -ep1 "+targetFile+" "+fileName;
			}else{
				cmd = cmd+" a -p- -ep1 "+targetFile+" "+fileName;
			}
			try{
				Runtime rtRuntime = Runtime.getRuntime();
				Process process = rtRuntime.exec(cmd);
				//等待完成
				System.out.println(process.waitFor());
			}catch (Exception e) {
				e.printStackTrace();
				System.out.println("压缩异常");
				isSuccess = false;
			}
		}else{//Linux操作系统
			
			String cmd = "";
			if(isPass){
				cmd = cmd+"/usr/local/bin/rar a -p"+password+" "+targetFile+" "+fileName;
			}else{
				cmd = cmd+"/usr/local/bin/rar a -p- "+targetFile+" "+fileName;
			}
			
			try {
				Runtime rt = Runtime.getRuntime();
				Process pun = rt.exec(cmd);
				System.out.println(pun.waitFor());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("压缩异常");
				isSuccess = false;
			}
			
		}
		
		return isSuccess;
	}
	
	/**
	 * 压缩成zip
	 * @param targetFile
	 * @param fileName
	 * @param isPass
	 * @return
	 */
	public static boolean toZip(String targetFile , String fileName , Boolean isPass){
		System.out.println("压缩文件�?"+fileName+"】到�?"+targetFile+"�?");
		if(targetFile == null || fileName == null || "".equals(targetFile) || "".equals(fileName))
			return false;
		File zipFile = new File(targetFile);
		File file = new File(fileName);
		if(zipFile.exists()){
			zipFile.canWrite();
			zipFile.delete();
		}
		try {
			zipFile.createNewFile();
			if(!file.exists()){
				return false;
			}
			
			ZipOutputStream out = new ZipOutputStream(zipFile);
			write(out, fileName, "");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static void write(ZipOutputStream out , String filePath , String base){
		File file = new File(filePath);
		if(file.exists()){
			if(file.isDirectory()){
				base = base.length() == 0 ? "" : base+File.separator;
				File[] fileList = file.listFiles();
				if(fileList != null && fileList.length > 0){
					for(int i = 0 ; i < fileList.length ; i++){
						write(out, fileList[i].getPath(), base+fileList[i].getName());
					}
				}
			}else{
				System.out.println(base);
				ZipEntry zipEntry = new ZipEntry(base);
				byte[] bi = new byte[2048];
				int cu = 0;
				InputStream input = null;
				try {
					out.putNextEntry(zipEntry);
					input = new FileInputStream(filePath);
					while((cu = input.read(bi)) != -1){
						out.write(bi, 0, cu);
					}
					input.close();
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	//格式化xml文件
	public static String formatXML(Document document, String charset) { 
        OutputFormat format = OutputFormat.createPrettyPrint(); 
        format.setEncoding(charset); 
        StringWriter sw = new StringWriter(); 
        XMLWriter xw = new XMLWriter(sw, format); 
        try { 
            xw.write(document); 
            xw.flush(); 
            xw.close(); 
        } catch (IOException e) { 
        	
        } 
        return sw.toString(); 
	}
	
	
	/**
	 * 将文件从�?个地方移动到另一个地�?
	 * 
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	public static void copyFileFromPlaceToPlace(InputStream input, OutputStream output)
			throws IOException {
		InputStream in = new BufferedInputStream(input, BUFFER_SIZE);
		OutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
		byte[] buffer = new byte[BUFFER_SIZE];
		int buffLen = in.read(buffer);
		while (buffLen > 0) {
			out.write(buffer, 0, buffLen);
			buffLen = in.read(buffer);
		}
		out.flush();
		out.close();
		in.close();
	}
	
	/**
	 * 创建目录
	 * @param dirPath
	 */
	public static File createDir(String dirPath){
		if(dirPath == null || "".equals(dirPath))
			return null;
		File file = new File(dirPath);
		if(!file.exists() || !file.isDirectory()){
			file.mkdirs();
		}
		return file;
	}
	
	/**
	 * 创建文件
	 * @param filePath
	 * @return
	 */
	public static File createFile(String filePath){
		if(filePath == null || "".equals(filePath))
			return null;
		File file = new File(filePath);
		if(!file.exists() || !file.isFile()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	/**
	 * 文件是否存在
	 * @param filePath
	 * @return
	 */
	public static boolean fileIsExit(String filePath){
		if(filePath == null || "".equals(filePath)){
			return false;
		}
		
		File file = new File(filePath);
		return file.exists();
	}
	
	/**
	 * 从文件路径中取出文件�?
	 */
	public static String takeOutFileName(String filePath) {
		if ("RAR".equals(filePath.substring(filePath.lastIndexOf(".") + 1)
				.toUpperCase())) {
			filePath = filePath.replaceAll("\\s{1,}", "");
		}
		int pos = filePath.lastIndexOf("/");
		if (pos > 0) {
			return filePath.substring(pos + 1);
		} else {
			int pos_s = filePath.lastIndexOf("\\");
			if (pos_s > 0) {
				return filePath.substring(pos_s + 1);
			} else {
				return filePath;
			}
		}
	}
	
	/**
	 * 从文件路径中取出文件路径
	 */
	public static String takeOutpath(String filePath) {
		int pos = filePath.indexOf(File.separator);
		int pos2 = filePath.lastIndexOf(File.separator);
		if ((pos > 0) && ((pos < pos2))) {
			return filePath.substring(pos + 1, pos2) + File.separator;
		} else {
			return "";
		}
	}
}
