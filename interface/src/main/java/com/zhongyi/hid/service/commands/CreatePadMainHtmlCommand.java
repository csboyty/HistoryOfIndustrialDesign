package com.zhongyi.hid.service.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.io.Closeables;
import com.zhongyi.hid.service.MakeBundleContext;
import com.zhongyi.hid.service.ThymeleafHtmlEngine;
import com.zhongyi.hid.service.commands.ToHtml.ToHtmlType;

/**
 * 
 * @author zzy
 * 
 */
public class CreatePadMainHtmlCommand implements Command {
	
	private final ThymeleafHtmlEngine htmlEngine;
	
	public CreatePadMainHtmlCommand(ThymeleafHtmlEngine htmlEngine){
		this.htmlEngine = htmlEngine;
	}
	

	@Override
	public boolean execute(Context context) throws Exception {
		MakeBundleContext makeBundleContext = (MakeBundleContext) context;
		Writer mainHtmlWriter = new BufferedWriter(new OutputStreamWriter
				(new FileOutputStream(new File(makeBundleContext.getTempBundleDir(),MakeBundleContext.MAIN_PAD_HTML_FILENAME)), "UTF-8"));
		org.thymeleaf.context.Context thyContext = null;
		try {
			ToHtml toHtml =new ToHtml(makeBundleContext,ToHtmlType.pad,mainHtmlWriter);
			thyContext = createThymeleafContext(makeBundleContext);
			toHtml.process(htmlEngine,thyContext);
			
		} finally {
			Closeables.closeQuietly(mainHtmlWriter);
		}
		return Command.CONTINUE_PROCESSING;
	}
	
	
	private org.thymeleaf.context.Context createThymeleafContext(MakeBundleContext makeBundleContext) throws IOException{
		
		org.thymeleaf.context.Context thyContext = new org.thymeleaf.context.Context();
		if("zyslide".equals(makeBundleContext.getCmsPost().getPostType())){
			thyContext.setVariable("slides", makeBundleContext.getSlides());
		}else{
			File postContentRevFile = new File(makeBundleContext.getTempBundleDir(),MakeBundleContext.POST_CONTENT_REV_FILENAME);
			String content = FileUtils.readFileToString(postContentRevFile);
			thyContext.setVariable("content", content);
		}
		thyContext.setVariable("title", makeBundleContext.getCmsPost().getTitle());
		thyContext.setVariable("year", "0000".equals(makeBundleContext.getBundleInfo().getYear())? "工业革命前" :makeBundleContext.getBundleInfo().getYear());
		thyContext.setVariable("genre",StringUtils.join(makeBundleContext.getBundleInfo().getGenre(),"，"));
		thyContext.setVariable("city", makeBundleContext.getBundleInfo().getCity());
		thyContext.setVariable("artists",StringUtils.join(makeBundleContext.getBundleInfo().getArtists(),"，"));
		thyContext.setVariable("organizations", StringUtils.join(makeBundleContext.getBundleInfo().getOrganizations(),"，"));
		thyContext.setVariable("description", makeBundleContext.getBundleInfo().getSummary());
		return thyContext;
	}
	
	
	
}