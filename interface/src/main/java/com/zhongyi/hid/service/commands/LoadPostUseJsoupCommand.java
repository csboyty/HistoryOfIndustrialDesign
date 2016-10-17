package com.zhongyi.hid.service.commands;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.zhongyi.hid.service.CmsDbRepository;
import com.zhongyi.hid.service.MakeBundleContext;
import com.zhongyi.hid.service.MakeBundleContext.CmsPost;
import com.zhongyi.hid.service.MakeBundleContext.CmsSlide;

/**
 * @Title: LoadPostUseJsoupCommand.java
 * @Package com.zhongyi.hid.service.commands
 * @Description: TODO(添加描述)
 * @author zhongzhenyang at gmail.com
 * @date 2013-9-11 上午10:55:46
 * @version V1.0
 */
public class LoadPostUseJsoupCommand  implements Command {
	
	private final CmsDbRepository cmsRepository;

	public LoadPostUseJsoupCommand(CmsDbRepository cmsRepository) {
		super();
		this.cmsRepository = cmsRepository;
	}
	
	@Override
	public boolean execute(Context context) throws Exception {
		MakeBundleContext makeBundleContext = (MakeBundleContext) context;
		File postContentTmp = new File(makeBundleContext.getTempBundleDir(),MakeBundleContext.POST_CONTENT_FILENAME);
		CmsPost cmsPost = CmsPost.create(postContentTmp);
		cmsRepository.readPost(makeBundleContext.getDocId(), cmsPost);
		makeBundleContext.setCmsPost(cmsPost);
		File postContentRev = new File(makeBundleContext.getTempBundleDir(),MakeBundleContext.POST_CONTENT_REV_FILENAME);
		JsoupProcessor jsoupProcessor = new JsoupProcessor(makeBundleContext.getDocId(),cmsPost.getPostType());
		jsoupProcessor.process(postContentTmp, postContentRev);
		makeBundleContext.getImages().addAll(jsoupProcessor.images);
		makeBundleContext.setSlides(jsoupProcessor.slides);
		makeBundleContext.setImage2MediaIdMap(jsoupProcessor.image2MediaIdMap);
		makeBundleContext.setName(cmsPost.getTitle());
		
		if("zyslide".equals(makeBundleContext.getCmsPost().getPostType()) && makeBundleContext.getSlides().size() > 0){
			cmsRepository.queryForSlide(makeBundleContext.getDocId(), makeBundleContext.getSlides());
		}
		
		
		return Command.CONTINUE_PROCESSING;
	}
	
	private static class JsoupProcessor {
		
		
		private final String postType;
		private final int docId;
		private final Set<String> images = Sets.newHashSet();
		private final List<CmsSlide> slides = Lists.newLinkedList();
		private final Map<String,String> image2MediaIdMap =Maps.newLinkedHashMap();
		
		private JsoupProcessor(int docId,String postType){
			this.docId = docId;
			this.postType = postType;
		}
		
		
		private void process(File inputFile,File outputFile) throws Exception{
			Document doc = Jsoup.parseBodyFragment(FileUtils.readFileToString(inputFile));
			if("zyslide".equals(postType)){
				processSlide(doc);
			}else{
				processArticle(doc);
				FileUtils.write(outputFile,doc.body().html(), "UTF-8");
			}
			
		}
		
		
		private void processSlide(Document document){
			Elements imageNodes = document.getElementsByTag("img");		
			for(Element image:imageNodes){
				String src = image.attr("src");
				images.add(src);
				String imageName = FilenameUtils.getName(src);
				//加载大图
				image.attr("src","images/"+imageName);
				String myMediaId = image.attr("data-zy-media-id");
				Element link = image.parent();
				String linkClass = null;
				String linkHref = null;
				if(link!=null && "a".equals(link.tagName())){
					linkClass = link.attr("class");
					linkHref = link.attr("href");
				}
				CmsSlide slide = new CmsSlide(docId,linkClass,linkHref,myMediaId,"images/"+imageName);
				slides.add(slide);
				if(!StringUtils.isEmpty(myMediaId)){
					image2MediaIdMap.put(imageName, myMediaId);
				}
			}
			
		}
		
		private void processArticle(Document document){
			Elements imageNodes = document.getElementsByTag("img");		
			for(Element image:imageNodes){
				Element link = image.parent();
				if(link!=null && "a".equals(link.tagName())){
					link.attr("data-zy-post-id", String.valueOf(docId));
				}
				String src = image.attr("src");
				images.add(src);
				String imageName = FilenameUtils.getName(src);
				image.attr("src","images/"+imageName);
				image.removeAttr("width").removeAttr("height").removeAttr("border");
				String myMediaId = image.attr("data-zy-media-id");
				if(!StringUtils.isEmpty(myMediaId)){
					image2MediaIdMap.put(imageName, myMediaId);
				}
			}
		}
	}
	
	
	

}

