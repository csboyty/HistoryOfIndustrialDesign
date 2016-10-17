package com.zhongyi.hid.service.commands;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.io.FilenameUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.XMLStreamWriter2;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Closeables;
import com.zhongyi.hid.service.CmsDbRepository;
import com.zhongyi.hid.service.MakeBundleContext;
import com.zhongyi.hid.service.MakeBundleContext.CmsPost;
import com.zhongyi.hid.service.MakeBundleContext.CmsSlide;
import com.zhongyi.hid.util.StaxXmlUtil;

/**
 * 1st command,load post content from cms db and correct image path
 * 
 * @author zzy
 * 
 */
public class LoadPostCommand implements Command {

	private final CmsDbRepository cmsRepository;

	public LoadPostCommand(CmsDbRepository cmsRepository) {
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
		PostContentCorrector corrector = new PostContentCorrector(makeBundleContext.getDocId(),cmsPost.getPostType());
		corrector.correct(postContentTmp, postContentRev);
		makeBundleContext.getImages().addAll(corrector.getImages());
		makeBundleContext.setSlides(corrector.getSlides());
		makeBundleContext.setImage2MediaIdMap(corrector.getImage2MediaIdMap());
		makeBundleContext.setName(cmsPost.getTitle());
		
		if("zyslide".equals(makeBundleContext.getCmsPost().getPostType()) && makeBundleContext.getSlides().size() > 0){
			cmsRepository.queryForSlide(makeBundleContext.getDocId(), makeBundleContext.getSlides());
		}
		
		
		return Command.CONTINUE_PROCESSING;
	}

	

	private static class PostContentCorrector {

		private final Set<String> images = Sets.newHashSet();
		private final List<CmsSlide> slides = Lists.newLinkedList();
		private final String postType;
		private final int docId;
		private String linkHref;
		private String linkClass;
		private final Map<String,String> image2MediaIdMap =Maps.newLinkedHashMap();
		
		private PostContentCorrector(int docId,String postType){
			this.docId = docId;
			this.postType = postType;
		}

		@SuppressWarnings("deprecation")
		private void correct(File inputFile, File outputFile)
				throws XMLStreamException, IOException {
			XMLInputFactory2 ifact = StaxXmlUtil.xmlInputFactory2();
			XMLOutputFactory2 of = StaxXmlUtil.xmlOutputFactory2();
			XMLStreamReader2 sr = (XMLStreamReader2) ifact.createXMLStreamReader(inputFile);
			OutputStream out = new BufferedOutputStream(new FileOutputStream(
					outputFile));
			XMLStreamWriter2 sw = (XMLStreamWriter2) of.createXMLStreamWriter(
					out, "UTF-8");
			try {
				for (int type = sr.getEventType(); type != XMLStreamConstants.END_DOCUMENT; type = sr
						.next()) {
					if (type == XMLStreamConstants.CHARACTERS) {
						sw.copyEventFromReader(sr, false);
					} else if (type == XMLStreamConstants.CDATA) {
						sw.writeCharacters(sr.getText());
					} else if (type == XMLStreamConstants.END_ELEMENT) {
						sw.writeEndElement();
					} else if (type == XMLStreamConstants.START_ELEMENT) {
						if("a".equals(sr.getName().getLocalPart())){
							sw.writeStartElement("a");
							if("zyslide".equals(postType)){
								linkClass = sr.getAttributeValue(null,"class");
								linkHref = sr.getAttributeValue(null, "href");
							}else{
								int attributeCount = sr.getAttributeCount();
								for (int i = 0; i < attributeCount; i++) {
									String attrName = sr.getAttributeName(i).getLocalPart();
									String attrValue = sr.getAttributeValue(i);
									sw.writeAttribute(attrName, attrValue);
								}
								sw.writeAttribute("data-zy-post-id",String.valueOf(docId));
							}
						}else if ("img".equals(sr.getName().getLocalPart())) {
							sw.writeStartElement("image");
							if("zyslide".equals(postType)){
								String imageSrc =sr.getAttributeValue(null, "src");
								images.add(imageSrc);
								String myMediaId = sr.getAttributeValue(null, "data-zy-media-id");
								CmsSlide slide = new CmsSlide(docId,linkClass,linkHref,myMediaId,"images/"+FilenameUtils.getName(imageSrc));
								slides.add(slide);
								linkClass = null;
								linkHref = null;
							}else{
								int attributeCount = sr.getAttributeCount();
								String imageName = null;
								String mediaId = null;
								for (int i = 0; i < attributeCount; i++) {
									String attrName = sr.getAttributeName(i)
											.getLocalPart();
									String attrValue = sr.getAttributeValue(i);
									
									if ("src".equalsIgnoreCase(attrName)) {
										images.add(attrValue);
										imageName = FilenameUtils.getName(attrValue);
										attrValue = "images/" + imageName;
										
									}
									
									if("data-zy-media-id".equalsIgnoreCase(attrName)){
										mediaId = attrValue;
									}
									
									if(!"width".equalsIgnoreCase(attrName) && !"height".equalsIgnoreCase(attrName))
										sw.writeAttribute(attrName, attrValue);
									
								}
								if(imageName !=null && mediaId !=null){
									image2MediaIdMap.put(imageName, mediaId);
								}
								
							}
						} else {
							sw.copyEventFromReader(sr, false);
						}
					}

					else {
						sw.copyEventFromReader(sr, false);
					}

				}
				sw.flush();
			} finally {
				sr.close();
				sw.close();
				Closeables.closeQuietly(out);
			}
		}

		public Set<String> getImages() {
			return images;
		}
		
		public List<CmsSlide> getSlides(){
			return slides;
		}

		public Map<String, String> getImage2MediaIdMap() {
			return image2MediaIdMap;
		}
		

	}

}
