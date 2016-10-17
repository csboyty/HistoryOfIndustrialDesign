package com.zhongyi.hid.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;

import com.zhongyi.hid.util.AppLogType;

/**
 * bundle complete handler,send notification to cms
 * 
 * @author zzy
 * 
 */
public class DefaultCompleteHandler {
	
	private final CmsDbRepository cmsDbRepository;

	private final HttpClient httpClient = new HttpClient();

	public DefaultCompleteHandler( CmsDbRepository cmsDbRepository) {
		this.cmsDbRepository =  cmsDbRepository;
		setupHttpClient();
	}

	private void setupHttpClient() {
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		httpClient.getParams().setCookiePolicy(CookiePolicy.DEFAULT);
		params.setSoTimeout(5000);
		params.setConnectionTimeout(60 * 1000);
		params.setDefaultMaxConnectionsPerHost(4);
		params.setMaxTotalConnections(8);
		MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
		manager.setParams(params);
		httpClient.setHttpConnectionManager(manager);

	}

	public void onSuccess(int docId) {
		int i = 0;
		while (i < SystemContextListener.getSystemProperty().getRetryTimes())
			try {
				String result = doCall(docId, true);
				if ("success".equals(result))
					break;
				else
					i++;
			} catch (Exception ex) {
				ex.printStackTrace();
				i++;
			}
		if(i >= SystemContextListener.getSystemProperty().getRetryTimes() ){
			cmsDbRepository.updateLockStatus(docId);
			cmsDbRepository.insertLog(AppLogType.TYPE_NOTIFY_CMS_ON_SUCC, "error", "bundle succ,[docId="+docId+"]", new Date());
		}

	}

	public void onException(int docId, Exception ex) {
		int i = 0;
		while (i < SystemContextListener.getSystemProperty().getRetryTimes())
			try {
				String result = doCall(docId, false);
				if ("success".equals(result))
					break;
				else
					i++;
			} catch (Exception e) {
				e.printStackTrace();
				i++;
			}
		if(i >= SystemContextListener.getSystemProperty().getRetryTimes()){
			cmsDbRepository.insertLog(AppLogType.TYPE_NOTIFY_CMS_ON_FAIL, "error", "bundle fail,[docId="+docId+"]", new Date());
		}
	}

	private String doCall(int docId, boolean success) throws IOException {

		PostMethod post = new PostMethod(SystemContextListener
				.getSystemProperty().getCmsNotificationURL());
		post.addParameter("action", "zy_pack_unlock");
		post.addParameter("docId", String.valueOf(docId));
		post.addParameter("packed_status", Boolean.toString(success));
		post.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		post.getParams().setParameter(
				HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(SystemContextListener
						.getSystemProperty().getRetryTimes(), false));
		int result = 0;
		InputStream in = null;
		byte[] resultBytes = null;
		try {
			result = httpClient.executeMethod(post);
			if (result == HttpStatus.SC_OK) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				in = post.getResponseBodyAsStream();
				IOUtils.copy(in, baos);
				resultBytes = baos.toByteArray();
			} else {
				throw new IOException("远程服务器错误,返回 HTTP状态码:" + result);
			}

		} catch (HttpException e) {
			throw new IOException(e);
		} finally {
			IOUtils.closeQuietly(in);
			post.releaseConnection();
		}
		String resultXml = new String(resultBytes, "UTF-8");
		System.out.println(docId + ":" +resultXml);
		return resultXml;
	}

}
