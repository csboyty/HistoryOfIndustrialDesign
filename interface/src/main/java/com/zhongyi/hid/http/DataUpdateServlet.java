package com.zhongyi.hid.http;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.common.io.Closeables;
import com.zhongyi.hid.service.DataUpdateService;
import com.zhongyi.hid.service.SystemContextListener;


@SuppressWarnings("serial")
public class DataUpdateServlet extends HttpServlet {
	
	private final String empty_xml = "<file_list/>";
	
	private final String empty_json = "[]";
	
	
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private DataUpdateService dataUpdateService;


	public void init(ServletConfig servletConfig)  throws ServletException {
		WebApplicationContext appCtx = WebApplicationContextUtils
				.getWebApplicationContext(servletConfig.getServletContext());
		dataUpdateService = BeanFactoryUtils.beanOfTypeIncludingAncestors(appCtx,DataUpdateService.class);
		super.init(servletConfig);

	}

	public void doGet(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws IOException{
		doPost(httpRequest,httpResponse);
	}

	public void doPost(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws IOException {
		String contentType;
		String requestURI = httpRequest.getRequestURI();
		String outputType ;
		String empty;
		if(requestURI.endsWith(".json")){
			contentType = "application/json;charset=UTF-8";
			outputType = "json";
			empty = empty_json;
		}else{
			contentType = "text/xml;charset=UTF-8";
			outputType = "xml";
			empty = empty_xml;
		}
		
		httpResponse.setContentType(contentType);
		String udid = httpRequest.getParameter("udid");
		String lastUpdateDateInMills = httpRequest.getParameter("lastUpdateDate");
		String category = httpRequest.getParameter("category");
		Writer writer = httpResponse.getWriter();
		Reader reader = null;
		try{
			if(!NumberUtils.isDigits(lastUpdateDateInMills)){
				if(logger.isInfoEnabled()){
					logger.info("dataUpdate error lastUpdateDate:"+lastUpdateDateInMills);
				}
				writer.write(empty);
			}else{
				long fromTimeInMills = Long.parseLong(lastUpdateDateInMills);
				long earliestInMills = SystemContextListener.getSystemProperty().getEarliestInMills();
				if( fromTimeInMills < earliestInMills ){
					fromTimeInMills = earliestInMills;
				}
				
				long toTimeInMills = System.currentTimeMillis();
			    reader = dataUpdateService.retriveDataUpdate(udid, fromTimeInMills,toTimeInMills,outputType);
			    if(reader == null){
			    	writer.write(empty);
			    }else{
			    	IOUtils.copy(reader, writer);
			    }
		    }
		    writer.flush();
		}catch(IllegalStateException ex){
			writer.write(empty);
			writer.flush();
			logger.error("retrive bundles system error", ex);
		}catch(IOException ex){
			logger.error("retrive bundles io error", ex);
		}finally{
			Closeables.closeQuietly(reader);
			Closeables.closeQuietly(writer);
		}

	}

}
