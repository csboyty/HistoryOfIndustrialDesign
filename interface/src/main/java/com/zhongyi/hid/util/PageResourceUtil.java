package com.zhongyi.hid.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletContext;

import com.zhongyi.hid.service.SystemContextListener;

public class PageResourceUtil {

	
	public static File getResource(String model,String resourceName) throws IOException{
		try{
			ServletContext servletContext = SystemContextListener.getServletContext();
			String  filepath = servletContext.getRealPath("/WEB-INF/pageStyles/"+model+"/"+resourceName);
			File file = new File(filepath);
			if(file.exists()){
				return file;
			}
			
			throw new FileNotFoundException("/WEB-INF/pageStyles/"+model+"/"+resourceName);
		}catch(IOException ex){
			throw ex;
		}
	}
	
	public static File findPublicDirecotry(String model){
			ServletContext servletContext = SystemContextListener.getServletContext();
			String  filepath = servletContext.getRealPath("/WEB-INF/pageStyles/"+model+"/public");
			File file = new File(filepath);
			if(file !=null && file.isDirectory()){
				return file;
			}
			return null;
	}
	
	

}
