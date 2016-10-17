package com.zhongyi.hid.service.commands;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import com.google.common.collect.Lists;
import com.google.common.io.Closeables;
import com.zhongyi.hid.dto.HidDocumentEntry;
import com.zhongyi.hid.dto.HidDocumentEntry.VideoItem;
import com.zhongyi.hid.service.CmsDbRepository;
import com.zhongyi.hid.service.MakeBundleContext;
import com.zhongyi.hid.service.SystemContextListener;
import com.zhongyi.hid.util.JsonUtil;
import com.zhongyi.hid.util.XmlUtil;

/**
 * 4th command,load doc info from cms db and yield doc.xml
 * @author zzy
 *
 */
public class CreateDocumentEntryCommand implements Command{
	
	private final CmsDbRepository cmsDbRepository;
	
	public CreateDocumentEntryCommand(CmsDbRepository cmsRepository) {
		super();
		this.cmsDbRepository = cmsRepository;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		MakeBundleContext makeBundleContext = (MakeBundleContext)context;
		HidDocumentEntry documentEntry = create(makeBundleContext);
		OutputStreamWriter docXmlWriter = new OutputStreamWriter(new FileOutputStream(makeBundleContext.getDocXmlFile()),"UTF-8");
		try{
			XmlUtil.toXml(documentEntry, docXmlWriter);
		}finally{
			Closeables.closeQuietly(docXmlWriter);
		}
		return Command.CONTINUE_PROCESSING;
	}
	
	private HidDocumentEntry create(MakeBundleContext context) throws IOException{
		HidDocumentEntry entry = new HidDocumentEntry();
		ArrayList<VideoItem> videoItemList =createVideoItemList(context);
		entry.setVideoItems(videoItemList);
		return entry;
		
	}
	

	
	private ArrayList<VideoItem> createVideoItemList(MakeBundleContext context){
		 int docId = context.getDocId();
		 Map<String,String> image2MediaIdMap =  context.getImage2MediaIdMap();
		 String showMediaUrlPrefix = SystemContextListener.getSystemProperty().getShowMediaUrlPrefix();
		 ArrayList<VideoItem> videoItemList = null;
		 if(!image2MediaIdMap.isEmpty()){
			videoItemList = Lists.newArrayList();
			Collection<String> metaKeys = image2MediaIdMap.values();
			Map<String,String> results =cmsDbRepository.queryForDataMedia(metaKeys);
			for(Map.Entry<String, String> entry : results.entrySet()){
				Map<String,Object> dataMediaObject = JsonUtil.getJsonObj(entry.getValue());
				String mediaType = (String)dataMediaObject.get("zy_media_type");
				if("zy_location_video".equals(mediaType)){
					String showUrl = showMediaUrlPrefix +docId+ "/"+entry.getKey();
					String videoUrl = (String) dataMediaObject.get("zy_media_filepath");
					videoItemList.add(new VideoItem(showUrl,videoUrl));
				}
			}
			
		 }
		 return videoItemList;
	}
	
	
	
	public static class CmsGenre {
		private final int id;
		private final String name;
		
		public CmsGenre(int id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}
		
		
		
	}
	

}
