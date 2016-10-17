package com.zhongyi.hid.service.commands;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import com.google.common.collect.Lists;
import com.zhongyi.hid.service.MakeBundleContext;
import com.zhongyi.hid.util.ZipUtil;


/**
 * 5th command, bundle tempBundleDir to zip package excepts .tmp file
 * @author zzy
 *
 */
public class MakeBundleCommand implements Command {

	@Override
	public boolean execute(Context context) throws Exception {
		
		MakeBundleContext makeBundleContext = (MakeBundleContext) context;
		bundleFile(makeBundleContext);
		return Command.CONTINUE_PROCESSING;
	}
	
	private void bundleFile(MakeBundleContext context) throws Exception{
		String filename = context.getDocIdAsMd5()+".zip";
		File zipFile = new File(context.getTempBundleDir().getParent(),filename);
		ZipUtil.zip(context.getTempBundleDir(), zipFile, Lists.newArrayList(new FileFilter(){

			@Override
			public boolean accept(File file) {
				if(file.isFile() && file.getName().endsWith("tmp"))
					return false;
				return true;
			}
			
		}));
		context.setBundleFile(zipFile);
	}
	

}
