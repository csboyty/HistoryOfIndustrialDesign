package com.zhongyi.hid.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.TemplateResolver;

import com.google.common.io.Closeables;

@Component
public class ThymeleafHtmlEngine {
	
	private TemplateEngine templateEngine;
	
	@Autowired
	private TemplateResolver templateResolver;
	
	@PostConstruct
	public void init(){
		templateResolver.setPrefix(SystemContextListener.getServletContext().getRealPath("/WEB-INF/pageStyles")+"/");
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
	}
	
	public void renderer(String tpl,IContext context,Writer writer){
		templateEngine.process(tpl, context,writer);
	}
	
	@SuppressWarnings("deprecation")
	public void renderer(String tpl,IContext context,File file){
	
		Writer writer =null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
			renderer(tpl,context,writer);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} finally{
			Closeables.closeQuietly(writer);
		}
	}
	
	
	

}
