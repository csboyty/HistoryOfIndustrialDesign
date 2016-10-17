package com.zhongyi.hid.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zhongyi.hid.dto.HidBundle;
import com.zhongyi.hid.dto.HidBundle.BundleInfo;
import com.zhongyi.hid.util.JsonUtil;

public class DataUpdateMockServlet extends HttpServlet {
	
	private final List<String> cache = Lists.newArrayList();
	
	private final File root = new File("D:/hid/bundles");
	
	private final Map<String,String> cities = Maps.newHashMap();
	
	private final List<String> cityNames = Lists.newArrayList();
	
	public DataUpdateMockServlet(){
		setupCities();
		setupBundles();
	}
	
	private void setupCities(){
		cities.put("巴黎", "20,40");
		cities.put("米兰", "40,50");
		cities.put("慕尼黑", "25,55");
		cities.put("马德里", "57,89");
		cities.put("伦敦", "50,10");
		cityNames.addAll(cities.keySet());
	}
	
	public void doGet(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws IOException{
		doPost(httpRequest,httpResponse);
	}
	
	public void doPost(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws IOException {
		httpResponse.setContentType("application/json;charset=UTF-8");
		Writer writer = httpResponse.getWriter();
		String lastUpdateDateInMills = httpRequest.getParameter("lastUpdateDate");
		int index =0;
		if(!StringUtils.isEmpty(lastUpdateDateInMills)){
			index = Integer.parseInt(lastUpdateDateInMills);
		}
		
		if(index != 0){
			index +=1;
		}
		
		if(index >= cache.size()){
			writer.write("[]");
		}else{
			writer.write("[");
			List<String> bundleJsonList =cache.subList(index, cache.size());
			Iterator<String> it =bundleJsonList.iterator();
			if(it.hasNext()){
				writer.write(it.next());
			}
			while(it.hasNext()){
				writer.write(",");
				writer.write(it.next());
			}
			writer.write("]");
			
		}
		writer.flush();
		
		
	}
	
	
	private void setupBundles(){
		for(int i =0;i< 100;i++){
			HidBundle bundle;
			try {
				bundle = makeBundle(i);
				cache.add(JsonUtil.toJson(bundle));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	
	}
	
	private HidBundle makeBundle(int id) throws IOException{
		HidBundle hidBundle = new HidBundle();
		String idMd5 = DigestUtils.md5Hex(String.valueOf(id));
		File bundleFile = new File(root,idMd5+".zip");
		
		hidBundle.setId(idMd5);
		hidBundle.setName("文章"+id);
		hidBundle.setOp("u");
		
		hidBundle.setTimestamp(String.valueOf(id));
		
		if(bundleFile.exists()){
			hidBundle.setSize(String.valueOf(FileUtils.sizeOf(bundleFile)));
			hidBundle.setUrl("http://192.168.2.147:8080/hid-bundle-app/download/"+bundleFile.getName());
			InputStream is = new FileInputStream(bundleFile);
			try{
				hidBundle.setMd5(DigestUtils.md5Hex(is));
			}finally{
				IOUtils.closeQuietly(is);
			}
		}
		BundleInfo info = new BundleInfo();
		String city = cityNames.get(id % cityNames.size());
		info.setCity(city);
		info.setCoordinate(cities.get(city));
		
		ArrayList<String> artists = Lists.newArrayList();
		ArrayList<String> organizations = Lists.newArrayList();
		
		int x = id % 8;
		for(int i=0;i<=x;i++){
			artists.add("artist-"+i);
			organizations.add("organ-"+i);
		}
		String genre = "流派-"+x;
		info.setGenre(Lists.newArrayList(genre));
		info.setArtists(artists);
		info.setOrganizations(organizations);
		
		int year =1800+id;
		
		Random r= new Random();

		year = year+r.nextInt(20);
		
		
		
		info.setYear(String.valueOf(year));
		info.setSummary("我是文章概要-"+id);
		hidBundle.setInfo(info);
		return hidBundle;
		
		
	}
	

}
