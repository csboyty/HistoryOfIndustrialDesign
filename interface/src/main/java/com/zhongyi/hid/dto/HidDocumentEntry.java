package com.zhongyi.hid.dto;

import java.util.ArrayList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


@Root(name = "doc")
public class HidDocumentEntry {
	
	@ElementList(name = "videoItems" ,required= false)
	private ArrayList<VideoItem> videoItems;
	
	
	public ArrayList<VideoItem> getVideoItems() {
		return videoItems;
	}

	public void setVideoItems(ArrayList<VideoItem> videoItems) {
		this.videoItems = videoItems;
	}



	@Root(name = "videoItem")
	public static class VideoItem{
		
		@Element(name = "showUrl")
		private String showUrl;
		
		@Element(name = "videoUrl")
		private String videoUrl;

		public VideoItem() {
			super();
		}

		public VideoItem(String showUrl, String videoUrl) {
			super();
			this.showUrl = showUrl;
			this.videoUrl = videoUrl;
		}

		public String getShowUrl() {
			return showUrl;
		}

		public void setShowUrl(String showUrl) {
			this.showUrl = showUrl;
		}

		public String getVideoUrl() {
			return videoUrl;
		}

		public void setVideoUrl(String videoUrl) {
			this.videoUrl = videoUrl;
		}
		
		
	}

	


}
