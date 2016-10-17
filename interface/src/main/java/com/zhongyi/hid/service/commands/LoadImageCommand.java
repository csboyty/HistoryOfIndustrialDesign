package com.zhongyi.hid.service.commands;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.zhongyi.hid.service.MakeBundleContext;
import com.zhongyi.hid.service.SystemContextListener;
import com.zhongyi.hid.util.Im4jUtil;


/**
 * 3rd command,find main.html which refers images and place them to images directory
 * @author zzy
 *
 */
public class LoadImageCommand implements Command {

	@Override
	public boolean execute(Context context) throws Exception {
		MakeBundleContext makeBundleContext = (MakeBundleContext) context;
		Collection<String> images =makeBundleContext.getImages();
		if(images != null && images.size() > 0){
			File imagesDir = new File(makeBundleContext.getTempBundleDir(),MakeBundleContext.IMAGES_DIR_NAME);
			if(!imagesDir.exists()){
				imagesDir.mkdir();
			}
			addImages(makeBundleContext,images,imagesDir);
		}
		return Command.CONTINUE_PROCESSING;
	}
	
	
	private void addImages(MakeBundleContext context,Collection<String> images,File imagesDir) throws Exception{
		
		String cmsWebRoot = SystemContextListener.getSystemProperty().getCmsWebRoot();
		String cmsContextPath = SystemContextListener.getSystemProperty().getCmsContextPath();
		
		if("zyslide".equals(context.getCmsPost().getPostType())){
			Integer imageWidth = SystemContextListener.getSystemProperty().getSlideImageWidth();
			if(imageWidth == -1){
				imageWidth = null;
			}
			
			Integer imageHeight =  SystemContextListener.getSystemProperty().getSlideImageHeight();
			if(imageHeight == -1){
				imageHeight = null;
			}
			
			if (imageWidth == null && imageHeight== null){
				throw new IllegalStateException("slide image width and height both not set");
			}
			
			
			Set<Pair<String,String>> pairs = new HashSet<Pair<String,String>>();
			for(String image:images){
				String imagePath =cmsWebRoot + File.separator + image.substring((image.indexOf(cmsContextPath) + cmsContextPath.length()));
				File compressImageFile = new File(new File(imagePath).getParent(),FilenameUtils.getName(image)+".small");
				if(!compressImageFile.exists()){
					Pair<String,String> pair= Pair.of(imagePath, compressImageFile.getAbsolutePath());
					pairs.add(pair);
				}
				//加载大图片
				FileUtils.copyFileToDirectory(new File(imagePath), imagesDir);
			}
			Im4jUtil.resizeImage(pairs, imageWidth, imageHeight);
		
		}else{
			for(String image:images){
				String imagePath =cmsWebRoot + File.separator + image.substring((image.indexOf(cmsContextPath) + cmsContextPath.length()));
				FileUtils.copyFileToDirectory(new File(imagePath), imagesDir);
			}
		
		}
		
		
		
	}

}
