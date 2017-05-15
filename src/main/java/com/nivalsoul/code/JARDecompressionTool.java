package com.nivalsoul.code;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;

/**
 * 解压jar文件
 * @author Administrator
 *
 */
public class JARDecompressionTool {
	
	public static void main(String[] args) {
		String path = "D:\\kettle\\lib";
		//decompress(path);
		checkFile(path, "NetRexxEngine");
	}
	
	
	private static void decompress(String path) {
		File file = new File(path);
		if(file.isFile() && path.endsWith(".jar")){
			String name = file.getName();
			String folder = new File(file.getParent(), name.substring(0, name.lastIndexOf('.'))).getAbsolutePath();
			decompress(path, folder);
			checkFile(folder, "HadoopFileInputMeta");
		}else if(file.isDirectory()){
			for (File f : file.listFiles()) {
				decompress(f.getAbsolutePath());
			}
		}
		
	}


	private static void checkFile(String folder, String filename) {
		File file = new File(folder);
		for (File f : file.listFiles()) {
			if (f.isFile()) {
				if(f.getName().indexOf(filename)!=-1)
					System.out.println("找到了！路径："+folder);
			}else{
				checkFile(f.getAbsolutePath(), filename);
			}
		}
		
	}


	public static synchronized void decompress(String fileName, String outputPath) {

		if (!outputPath.endsWith(File.separator)) {
			outputPath += File.separator;
		}
		File dir = new File(outputPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		JarFile jf = null;
		try {
			jf = new JarFile(fileName);
			for (Enumeration<JarEntry> e = jf.entries(); e.hasMoreElements();) {
				JarEntry je = (JarEntry) e.nextElement();
				String outFileName = outputPath + je.getName();
				File f = new File(outFileName);
				if (je.isDirectory()) {
					if (!f.exists()) {
						f.mkdirs();
					}
				} else {
					File pf = f.getParentFile();
					if (!pf.exists()) {
						pf.mkdirs();
					}
					InputStream in = jf.getInputStream(je);
					OutputStream out = new BufferedOutputStream(new FileOutputStream(f));
					byte[] buffer = new byte[2048];
					int nBytes = 0;
					while ((nBytes = in.read(buffer)) > 0) {
						out.write(buffer, 0, nBytes);
					}
					out.flush();
					out.close();
					in.close();
				}
			}
		} catch (Exception e) {
			System.out.println("解压" + fileName + "出错---" + e.getMessage());
		} finally {
			/*if (jf != null) {
				try {
					jf.close();
					File jar = new File(jf.getName());
					if (jar.exists()) {
						jar.delete();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}*/
		}
	}
}
