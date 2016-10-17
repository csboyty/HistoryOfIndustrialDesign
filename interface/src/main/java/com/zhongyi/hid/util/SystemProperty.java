package com.zhongyi.hid.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;

public class SystemProperty {

	private final long earliestInMills;
	private final String tablePrefix;
	private final String cmsNotificationURL;
	private final String cmsWebRoot;
	private final String cmsContextPath;
	private final String bundleRoot;
	private final String bundleRootURL;
	private final int retryTimes;
	private final String thumbSize;
	private final String showMediaUrlPrefix;
	private final int slideImageWidth;
	private final int slideImageHeight;

	private SystemProperty(InputStream inputStream) throws IOException {
		Properties properties = new Properties();
		properties.load(inputStream);
		tablePrefix = properties.getProperty("table_prefix", "");
		cmsNotificationURL = properties.getProperty("cms_notification_url");
		cmsWebRoot = properties.getProperty("cms_web_root");
		cmsContextPath = properties.getProperty("cms_context_path");
		bundleRoot = properties.getProperty("bundle_root");
		bundleRootURL = properties.getProperty("bundle_root_url");
		retryTimes = Integer.parseInt(properties.getProperty("retry_times", "3"));
		thumbSize = properties.getProperty("thumb_size");
		showMediaUrlPrefix = properties.getProperty("show_media_url_prefix");
		slideImageWidth =Integer.parseInt(properties.getProperty("slide_image_width"));
		slideImageHeight =Integer.parseInt(properties.getProperty("slide_image_height"));
		
		
		checkNotNull();
		Calendar c =  Calendar.getInstance();
		c.set(2013, 6, 1, 0, 0);
		earliestInMills=c.getTimeInMillis();
	}

	private void checkNotNull() {
		if (tablePrefix == null || cmsNotificationURL == null
				|| cmsWebRoot == null || cmsContextPath == null || bundleRoot ==null)
			throw new IllegalStateException(
					"SystemProperty create fail,some properties is null.");
	}

	public String getTablePrefix() {
		return tablePrefix;
	}

	public String getCmsNotificationURL() {
		return cmsNotificationURL;
	}

	public String getCmsWebRoot() {
		return cmsWebRoot;
	}

	public String getCmsContextPath() {
		return cmsContextPath;
	}

	
	public String getBundleRoot() {
		return bundleRoot;
	}

	
	public String getBundleRootURL() {
		return bundleRootURL;
	}

	public int getRetryTimes() {
		return retryTimes;
	}
	
	public long getEarliestInMills() {
		return earliestInMills;
	}
	

	public String getThumbSize() {
		return thumbSize;
	}
	

	public String getShowMediaUrlPrefix() {
		return showMediaUrlPrefix;
	}

	public int getSlideImageWidth() {
		return slideImageWidth;
	}

	public int getSlideImageHeight() {
		return slideImageHeight;
	}

	public static SystemProperty create(InputStream inputStream)
			throws IOException {
		return new SystemProperty(inputStream);
	}

	
}
