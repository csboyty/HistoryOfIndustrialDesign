package com.zhongyi.hid.service.commands;

import java.io.File;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.zhongyi.hid.service.MakeBundleContext;
import com.zhongyi.hid.service.SystemContextListener;
import com.zhongyi.hid.util.Im4jUtil;
import com.zhongyi.hid.util.JsonUtil;

/**
 * @Title: ProfileResizeCommand.java
 * @Package com.zhongyi.hid.service.commands
 * @Description: TODO(添加描述)
 * @author zhongzhenyang at gmail.com
 * @date 2013-9-27 下午5:27:48
 * @version V1.0
 */
public class ProfileResizeCommand implements Command{
	
	private final Iterable<Pair<Integer,Integer>> resizeOptions;
	private final String cmsWebRoot;
	private final String cmsContextPath;
	
	public ProfileResizeCommand(){
		InputStream in = SystemContextListener.getServletContext().getResourceAsStream("/WEB-INF/profile_opts.json");
		List<Object> resizeList = JsonUtil.parseArray(in);
		Set<Pair<Integer,Integer>> resizeSet = new LinkedHashSet<Pair<Integer,Integer>>();
		for(Object o :resizeList){
			String[] e = ((String)o).split("x");
			resizeSet.add(Pair.<Integer,Integer>of(Integer.parseInt(e[0]), Integer.parseInt(e[1])));
		}
		resizeOptions = resizeSet;
		cmsWebRoot = SystemContextListener.getSystemProperty().getCmsWebRoot();
		cmsContextPath = SystemContextListener.getSystemProperty().getCmsContextPath();
		
	}

	@Override
	public boolean execute(Context context) throws Exception {
		MakeBundleContext makeBundleContext = (MakeBundleContext) context;
		String profilePath = makeBundleContext.getBundleInfo().getProfile();
		String orignImagePath =cmsWebRoot + File.separator + profilePath.substring((profilePath.indexOf(cmsContextPath) + cmsContextPath.length()));
		int lastDotPos = orignImagePath.lastIndexOf(".");
		for(Pair<Integer,Integer> resizeOpt: resizeOptions){
			String compressImagePath = orignImagePath.substring(0,lastDotPos)+"-"+resizeOpt.getLeft()+"x"+resizeOpt.getRight()+orignImagePath.substring(lastDotPos);
			if(! new File(compressImagePath).exists()){
				Im4jUtil.resizeImage(Lists.newArrayList(Pair.<String,String>of(orignImagePath, compressImagePath)), resizeOpt.getLeft(), resizeOpt.getRight());
			}
						
		}
		return Command.CONTINUE_PROCESSING;
	}
	
	
	

}

