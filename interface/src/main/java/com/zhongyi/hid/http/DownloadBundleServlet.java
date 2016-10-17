package com.zhongyi.hid.http;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class DownloadBundleServlet extends HttpServlet {
	
	private final File root = new File("D:/hid/bundles");
	
	public void doGet(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws IOException{
		doPost(httpRequest,httpResponse);
	}
	
	public void doPost(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws IOException {
		
		String filename =FilenameUtils.getName(httpRequest.getRequestURI());
		File bundleFile = new File(root,filename);
		if(!bundleFile.exists()){
			httpResponse.sendError(404);
		}else{
			httpResponse.setContentType("application/octet-stream");
			OutputStream os =httpResponse.getOutputStream();
			FileUtils.copyFile(bundleFile, os);
			os.flush();
			os.close();
		}
		
		
		
	}

}
