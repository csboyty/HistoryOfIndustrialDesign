package com.zhongyi.hid.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.common.io.Closeables;
import com.zhongyi.hid.service.BundleManageService;

@SuppressWarnings("serial")
public class RemoveBundleServlet extends HttpServlet{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private BundleManageService bundleManageService;

	public void init(ServletConfig servletConfig) throws ServletException {
		WebApplicationContext appCtx = WebApplicationContextUtils
				.getWebApplicationContext(servletConfig.getServletContext());
		bundleManageService = BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx, BundleManageService.class);
		super.init(servletConfig);
	}
	
	public void doGet(HttpServletRequest httpRequest,HttpServletResponse httpResponse) throws IOException{
		doPost(httpRequest,httpResponse);
	}
	
	@SuppressWarnings("deprecation")
	public void doPost(HttpServletRequest httpRequest,HttpServletResponse httpResponse) throws IOException{
		String result = "success";
		String docId ="";
		try{
			long now = System.currentTimeMillis();
			docId = httpRequest.getParameter("docId");
			String[] docIdArray=StringUtils.split(docId, ",");
			bundleManageService.removeBundles(docIdArray,now);
		}catch(Exception ex){
			logger.error("remove bundle failed,docId:"+docId, ex);
			result = "failure";
		}
		
		httpResponse.setContentType("text/plain;charset=UTF-8");
		PrintWriter writer =httpResponse.getWriter();
		try{
			writer.print(result);
		}finally{
			Closeables.closeQuietly(writer);
		}
	}

}
