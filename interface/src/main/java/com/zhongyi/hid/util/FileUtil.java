package com.zhongyi.hid.util;

import java.io.File;

public class FileUtil {
	
	private static final File _appTempDir;
	
	
	static{
		_appTempDir = new File(System.getProperty("java.io.tmpdir"), "hid-bundles");
		if(!_appTempDir.exists()) _appTempDir.mkdir();
		_appTempDir.deleteOnExit();
		
	}
	
	public static File appTempDir(){
		return _appTempDir; 
	}
	
	

	public static File tempDir() {
		File dir = new File(_appTempDir,String.valueOf(System.nanoTime()));
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
	
	
	public static File tempBundleDir(String bundleName){
		File parent = tempDir();
		File bundleDir = new File(parent,bundleName);
		if(!bundleDir.exists()){
			bundleDir.mkdir();
		}
		return bundleDir;
	}
	

}
