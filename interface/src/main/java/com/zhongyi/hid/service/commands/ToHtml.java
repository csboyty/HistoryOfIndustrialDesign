package com.zhongyi.hid.service.commands;

import java.io.File;
import java.io.Writer;

import org.apache.commons.io.FileUtils;
import org.thymeleaf.context.IContext;

import com.zhongyi.hid.service.MakeBundleContext;
import com.zhongyi.hid.service.ThymeleafHtmlEngine;
import com.zhongyi.hid.util.PageResourceUtil;

class ToHtml {
	
	public enum ToHtmlType{
		pad,phone
	}
	
	private final MakeBundleContext context;
	private final ToHtmlType toHtmlType;
	private final Writer mainHtmlWriter;

	ToHtml(MakeBundleContext context,ToHtmlType toHtmlType,Writer mainHtmlWriter) {
		this.context = context;
		this.toHtmlType = toHtmlType;
		this.mainHtmlWriter = mainHtmlWriter;
	}

	public void process(ThymeleafHtmlEngine engine,IContext thyContext){
		loadCss();
		loadJs();
		loadAsset();
		rendererHtml(engine,thyContext);
	}
	

	
	private void loadCss() {
		String model = "zyslide".equals(context.getCmsPost().getPostType()) ? "slide": "article";
		String mainCssName = toHtmlType == ToHtmlType.pad ? "main.pad.css" : "main.phone.css";
		try {
			File mainCss = PageResourceUtil.getResource(model, mainCssName);
			FileUtils.copyFileToDirectory(mainCss, context.getPublicDir());
		} catch (Exception e) {
			throw new IllegalStateException("load css error", e);
		} 
		
		
	}

	private void loadJs() {
		String model = "zyslide".equals(context.getCmsPost().getPostType()) ? "slide": "article";
		String mainJsName = toHtmlType == ToHtmlType.pad ? "main.pad.js" :"main.phone.js";
		try {
			File mainJs = PageResourceUtil.getResource(model,mainJsName);
			FileUtils.copyFileToDirectory(mainJs, context.getPublicDir());
		} catch (Exception e) {
			throw new IllegalStateException("load js", e);
		} 
	}

	public void loadAsset(){
		String model = "zyslide".equals(context.getCmsPost().getPostType()) ? "slide": "article";
		String assetDirName = toHtmlType == ToHtmlType.pad ? "asset.pad" :"asset.phone";
		try {
			File assetDir = PageResourceUtil.getResource(model,assetDirName);
			File bundleAssetDir = new File(context.getTempBundleDir(),MakeBundleContext.ASSET_DIR_NAME);
			if(!bundleAssetDir.exists())
				bundleAssetDir.mkdir();
			File[] files =assetDir.listFiles();
			for(File file:files){
				FileUtils.copyFileToDirectory(file, bundleAssetDir);
			}
		} catch (Exception e) {
			throw new IllegalStateException("load asset", e);
		} 
	}
	
	private void rendererHtml(ThymeleafHtmlEngine engine,IContext thyContext) {
		String model = "zyslide".equals(context.getCmsPost().getPostType()) ? "slide": "article";
		String mainHtmlName = toHtmlType == ToHtmlType.pad ? "main.pad" :"main.phone";
		String tpl = model+"/"+ mainHtmlName;
		
		try {
			engine.renderer(tpl,thyContext, mainHtmlWriter);
		} catch (Exception e) {
			throw new IllegalStateException("renderer html error", e);
		}
	}

	

}
