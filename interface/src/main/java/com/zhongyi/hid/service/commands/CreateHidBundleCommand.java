package com.zhongyi.hid.service.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import com.google.common.io.Closeables;
import com.zhongyi.hid.dto.HidBundle;
import com.zhongyi.hid.service.CmsRedisRepository;
import com.zhongyi.hid.service.MakeBundleContext;
import com.zhongyi.hid.service.SystemContextListener;

public class CreateHidBundleCommand implements Command {
	
	private CmsRedisRepository cmsRedisRepository;
	
	
	public CreateHidBundleCommand(CmsRedisRepository cmsRedisRepository){
		this.cmsRedisRepository = cmsRedisRepository;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		MakeBundleContext makeBundleContext = (MakeBundleContext) context;
		HidBundle hidBundle = generateBundle(makeBundleContext);
		cmsRedisRepository.addBundle(hidBundle);
		return Command.PROCESSING_COMPLETE;
	}
	
	
	private HidBundle generateBundle(MakeBundleContext context) throws Exception {
		
		HidBundle bundle = new HidBundle();
		InputStream bundleInputStream = new FileInputStream(context.getBundleFile());
		try {
			bundle.setMd5(DigestUtils.md5Hex(bundleInputStream));
		} finally {
			Closeables.closeQuietly(bundleInputStream);
		}
		bundle.setId(String.valueOf(context.getDocId()));
		bundle.setOp("u");
		bundle.setSize(String.valueOf(FileUtils.sizeOf(context.getBundleFile())));
		bundle.setName(context.getName());
		bundle.setTimestamp(String.valueOf(System.currentTimeMillis()));
		bundle.setUrl(context.getBundleFile().getName());
		bundle.setInfo(context.getBundleInfo());
		FileUtils.copyFileToDirectory(context.getBundleFile(), new File(SystemContextListener.getSystemProperty().getBundleRoot()), false);
		return bundle;
	}
	

}
